package com.jjaln.dailychart;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jjaln.dailychart.Recycler.coinList.Coin_List_Data;
import com.jjaln.dailychart.Recycler.coinList.Coin_List_RecyclerAdapter;
import com.jjaln.dailychart.Recycler.exchange.Exchange_List_Data;
import com.jjaln.dailychart.Recycler.exchange.Exchange_List_RecyclerAdapter;
import com.jjaln.dailychart.contents.dashboard.UserDashBoardActivity;
import com.jjaln.dailychart.wallet.Api_Client;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private Context mContext = MainActivity.this;

    private Toolbar toolbarNomad;
    private ImageView ivMenu;
    private DrawerLayout drawer;
    private NavigationView nv;
    private Coin_List_RecyclerAdapter coinAdapter;
    private LinearLayoutManager coinManager;
    private TextView tv_currentAsset;
    private RecyclerView rvCoin, rvExchange;
    private SharedPreferences pref;
    private String token;
    private ArrayList<Coin_List_Data> CoinData;
    private FirebaseUser user;
    private RoundedImageView rivUser;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("/////////","oncreate start");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbarNomad = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbarNomad);

        ivMenu = findViewById(R.id.iv_back);
        tv_currentAsset = findViewById(R.id.tv_currentAsset);
        drawer = findViewById(R.id.drawer);
        rivUser = (RoundedImageView) findViewById(R.id.riv_user);

        ivMenu.setOnClickListener(v -> {
            drawer.openDrawer(Gravity.LEFT);
        });

        LayoutInflater mInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(R.layout.navigation, drawer, true);
        nv = findViewById(R.id.nv);

        com.jjaln.dailychart.NavigationViewHelper.enable(MainActivity.this, nv);

        //Exchage List
        rvExchange = findViewById(R.id.rv_exchangeList);
        ArrayList<Exchange_List_Data> ExchangeData = new ArrayList<>();

        ExchangeData.add(new Exchange_List_Data(R.mipmap.bithumb, "com.btckorea.bithumb", "Bithumb"));
        ExchangeData.add(new Exchange_List_Data(R.mipmap.upbit, "com.dunamu.exchange", "Upbit"));

        LinearLayoutManager exchange_manager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        rvExchange.setLayoutManager(exchange_manager);
        rvExchange.setAdapter(new Exchange_List_RecyclerAdapter(ExchangeData));

        coinManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        rvCoin = findViewById(R.id.rv_CoinList);

        Log.d("/////////","oncreate End");
    }

    @Override
    protected void onResume() {

        super.onResume();
        Log.d("/////////","OnResume Start");
        pref = getSharedPreferences("pref", MODE_PRIVATE);
        token = pref.getString("token", "");

        if (token.equals("")) {
            nv.getMenu().findItem(R.id.login).setVisible(true);
            nv.getMenu().findItem(R.id.dashboard).setVisible(false);
        } else {
            nv.getMenu().findItem(R.id.login).setVisible(false);
            nv.getMenu().findItem(R.id.dashboard).setVisible(true);
        }
        appbarRight();
        NetworkThread thread = new NetworkThread();
        thread.start();
        Log.d("/////////","OnResume End");
    }

    private void appbarRight() {
        if (token.equals("")) {
            rivUser.setVisibility(View.INVISIBLE);
        } else {
            rivUser.setVisibility(View.VISIBLE);

            user = FirebaseAuth.getInstance().getCurrentUser();
            Glide
                    .with(mContext)
                    .load(user.getPhotoUrl())
                    .placeholder(R.drawable.ic_user)
                    .into(rivUser);
            rivUserClick();
        }
    }

    private void rivUserClick() {
        rivUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu p = new PopupMenu(
                        getApplicationContext(),//화면제어권자
                        v);             //팝업을 띄울 기준이될 위젯
                getMenuInflater().inflate(R.menu.user_menu, p.getMenu());
                //이벤트 처리
                p.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getTitle().equals("Dashboard")) {
                            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(v.getContext(), UserDashBoardActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            v.getContext().startActivity(intent);
                        } else {
                            FirebaseAuth.getInstance().signOut();
                            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("token", "");
                            editor.commit();
                            Intent intent = new Intent(v.getContext(), MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            v.getContext().startActivity(intent);
                        }
                        return false;
                    }
                });
                p.show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    class NetworkThread extends Thread {
        @Override
        public void run() {
            try {
                Log.d("/////////","Thread Start");
                Api_Client api = new Api_Client("a10c1f984334fb14c30ebaf3e60ce998",
                        "32fe2aae6de50ec84b0ed4cf6093a95b");
                HashMap<String, String> rgParams = new HashMap<String, String>();
                rgParams.put("currency", "ALL");


                // API 를 이용하여 info-balance 의 결과값을 JSON 타입으로 가져오기
                final String result = api.callApi("/info/balance", rgParams);
                // JSONObject 객체에 담는다.
                JSONObject obj = new JSONObject(result);
                String status = obj.getString("status");
                // 'data' 객체는 Object
                JSONObject data_list = obj.getJSONObject("data");
                String total_krw = data_list.getString("total_krw");
                String in_use_krw = data_list.getString("in_use_krw");
                String available_krw = data_list.getString("available_krw");
                String total_btc = data_list.getString("total_btc");
                String in_use_btc = data_list.getString("in_use_btc");
                String available_btc = data_list.getString("available_btc");
                String total_eth = data_list.getString("total_eth");
                String in_use_eth = data_list.getString("in_use_eth");
                String available_eth = data_list.getString("available_eth");
                String total_xrp = data_list.getString("total_xrp");
                String in_use_xrp = data_list.getString("in_use_xrp");
                String available_xrp = data_list.getString("available_xrp");
                String total_dot = data_list.getString("total_dot");
                String in_use_dot = data_list.getString("in_use_dot");
                String available_dot = data_list.getString("available_dot");
                String total_ada = data_list.getString("total_ada");
                String in_use_ada = data_list.getString("in_use_ada");
                String available_ada = data_list.getString("available_ada");
                String xcoin_last_btc = data_list.getString("xcoin_last_btc");
                String xcoin_last_eth = data_list.getString("xcoin_last_eth");
                String xcoin_last_xrp = data_list.getString("xcoin_last_xrp");
                String xcoin_last_dot = data_list.getString("xcoin_last_dot");
                String xcoin_last_ada = data_list.getString("xcoin_last_ada");

                //double total_bithumb = Integer.parseInt(total_krw) +

                float balance = Float.parseFloat(total_krw) + (Float.parseFloat(xcoin_last_btc) * Float.parseFloat(total_btc)) +
                        (Float.parseFloat(xcoin_last_eth) * Float.parseFloat(total_eth)) +
                        (Float.parseFloat(xcoin_last_xrp) * Float.parseFloat(total_xrp)) +
                        (Float.parseFloat(xcoin_last_ada) * Float.parseFloat(total_ada)) +
                        (Float.parseFloat(xcoin_last_dot) * Float.parseFloat(total_dot));
                String Asset = "Bithumb : " + balance;

                String[] coin_list = {"BTC", "ETH", "XRP", "ADA", "DOT"};
                ArrayList<String> coins = new ArrayList<>(Arrays.asList(coin_list));
                int[] coin_img = {R.mipmap.btc, R.mipmap.eth, R.mipmap.xrp, R.mipmap.ada, R.mipmap.dot};
                CoinData = new ArrayList<>();
                for (String coin : coin_list) {
                    final String res = api.callApi("/public/ticker/" + coin + "/KRW", rgParams);
                    JSONObject object = new JSONObject(res);
                    JSONObject dt_list = object.getJSONObject("data");
                    int c_img = coin_img[coins.indexOf(coin)];
                    String closing_price = dt_list.getString("closing_price");
                    String fluctate_24H = dt_list.getString("fluctate_24H");
                    String fluctate_rate_24H = dt_list.getString("fluctate_rate_24H");
                    CoinData.add(new Coin_List_Data(c_img, coin, closing_price, fluctate_rate_24H, fluctate_24H));
                    coinAdapter = new Coin_List_RecyclerAdapter(CoinData,mContext);
                    Log.d("//////////", closing_price + " / " + fluctate_24H);
                }
                rvCoin.setLayoutManager(coinManager);
                rvCoin.setAdapter(coinAdapter);

                tv_currentAsset.setText(Asset);
                Log.d("/////////","Thread Start");
            } catch (Exception e) {
                Log.d("/////////", "DataGetError");
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
    }
}