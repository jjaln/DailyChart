import time
import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore
from GoogleNews import GoogleNews
import ssl
from multiprocessing import Pool
import random
#검색하고자 하는 코인 리스트
search_keyword =['BTC','XRP','ETH','폴카닷','카르다노']
coin_list = ['비트코인','리플','이더리움','폴카닷','카르다노']
coin_eng = ['BTC','XRP','ETH','DOT','ADA']
coin_index = [0]*len(coin_list)
news_pages = [0]*len(coin_list)
#파이어 베이스 연동 
cred = credentials.Certificate("mptp.json")
# firebase = firebase_admin.initialize_app(cred, {
#     'databaseURL': "https://test-9ec41-default-rtdb.firebaseio.com/"
# })
#파이어 스토어 연동
fire_store = firebase_admin.initialize_app(cred,{ 'projectID':'mptp-9e803'})

#SSL 인증 승인
ssl._create_default_https_context = ssl._create_unverified_context
db = firestore.client()

def crawl(coin):
    page = news_pages[search_keyword.index(coin)]
    news = GoogleNews(lang='ko',encode='utf-8')
    news.search(coin)
    time.sleep(30)
    news.getpage(page)
    title = news.get_texts()
    url = news.get_links()
    desc = news.get_desc()
    for t, u, d in zip(title,url,desc):
        # print(d)
        idx = coin_index[search_keyword.index(coin)]
        if t!= "" and u != "" and d != "":
            dic = {u"title" : u'{}'.format(t),u"desc" : u'{}'.format(d), u"link": u'{}'.format(u)}
            if coin_list[search_keyword.index(coin)] in t or coin_eng[search_keyword.index(coin)] in t:
                if idx == 0:
                    ref = db.collection(u'{}'.format(coin_eng[search_keyword.index(coin)]))
                    ref.add(dic)
                    time.sleep(random.uniform(2,4))
                    coin_index[search_keyword.index(coin)] += 1
                else:
                    flag = True
                    ref = db.collection(u'{}'.format(coin_eng[search_keyword.index(coin)])).stream()
                    for doc in ref:
                        time.sleep(random.uniform(1,3))
                        check_dic = doc.to_dict()
                        #print('[check] {}'.format(check_dic))
                        if dic['title'] == check_dic['title']: 
                            flag = False
                            break
                    if flag:
                        print('[{}] ///// {} '.format(coin,dic))
                        ref = db.collection(u'{}'.format(coin_eng[search_keyword.index(coin)]))
                        ref.add(dic)
                        time.sleep(random.uniform(1,5))
                        #print(coin,t,u)
                        coin_index[search_keyword.index(coin)] += 1
    news_pages[search_keyword.index(coin)] += 1

if __name__=="__main__":
    while True:
        pool=Pool(processes=3)
        pool.map(crawl,search_keyword)
        time.sleep(2)
        pool.close()
        pool.join()