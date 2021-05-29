package com.jjaln.dailychart.contents;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jjaln.dailychart.R;
import com.jjaln.dailychart.adapter.NewsListAdapter;
import com.jjaln.dailychart.feature.News;
import com.jjaln.dailychart.wallet.Api_Client;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CoinInfo extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private NewsListAdapter mNewsAdapter;
    private LinearLayoutManager layoutManager;
    private ArrayList<News> newsListData;
    private ImageView ivBack;
    private Context context;
    private FirebaseFirestore database;
    private String coin_type, coin_name;
    private int coin_img;
    private Toolbar toolbar;
    List<Integer> price_list = new ArrayList<Integer>();
    public GraphView graph;
    DataPoint[] pricePoints;
    LineGraphSeries series;
    int cnt = 30;
    private TextView current_price, percentage,percentage_price, tv_CoinName, tv_CoinType;
    private ImageView iv_coinimg;
    DecimalFormat in = new DecimalFormat("###,###");
    DecimalFormat dot1 = new DecimalFormat("###.#");
    DecimalFormat dot2 = new DecimalFormat("###.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_info);

        graph = (GraphView) findViewById(R.id.chart);
        context = this;
        toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        ivBack = findViewById(R.id.iv_back);
        ivBack.setOnClickListener(v -> {
            finish();
        });
        graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.NONE);
        graph.getGridLabelRenderer().setVerticalLabelsVisible(false);
        graph.getGridLabelRenderer().setHorizontalLabelsVisible(false);
        Intent intent = getIntent();
        coin_name = intent.getExtras().getString("coin_type");
        coin_type = intent.getExtras().getString("coin_type");
        coin_img = intent.getExtras().getInt("coin_img");
        RoundedImageView imageView = (RoundedImageView) findViewById(R.id.riv_coin);
        TextView textView = findViewById(R.id.tv_coininfo);

        layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        mRecyclerView = findViewById(R.id.rv_news);

        imageView.setImageResource(coin_img);textView.setText(coin_type);
        tv_CoinName = findViewById(R.id.coin_name);
        tv_CoinType = findViewById(R.id.coin_type);
        iv_coinimg = findViewById(R.id.coin_image);

        getNews();
    }

    private void getNews() {
        FirebaseFirestore db = database.getInstance();

        CollectionReference Ref = db.collection(coin_type);
        Ref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                newsListData = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Map<String, Object> data = document.getData();
                    String title = data.get("title").toString();
                    String url = data.get("link").toString();
                    String desc = data.get("desc").toString();
                    News cData = new News(title, desc, url);
                    newsListData.add(cData);
                    mNewsAdapter = new NewsListAdapter(newsListData, context);
                }
                mRecyclerView.setAdapter(mNewsAdapter);
                mRecyclerView.setLayoutManager(layoutManager);
            }
        });
        NetworkThread thread = new NetworkThread();
        thread.start();
    }


    public synchronized void drawLineGraph() throws Exception {
        pricePoints = new DataPoint[price_list.size()];

        for (int i = 0; i < pricePoints.length; i++) {
            pricePoints[i] = new DataPoint(i, price_list.get(i));
        }
        series = new LineGraphSeries<>(pricePoints);
        // 그래프에 데이터 점들 붙이기
        graph.addSeries(series);
        graph.getViewport().setMinY(price_list.get(0) - price_list.get(0)*0.002);
        graph.getViewport().setMaxY(price_list.get(0) + price_list.get(0)*0.002);
        graph.getViewport().setYAxisBoundsManual(true);
        // x축의 연장 가능성 true
        graph.getViewport().setXAxisBoundsManual(true);
        // 데이터가 늘어날때 x축의 scroll 이 생기도록 설정
        graph.getViewport().setScrollable(true);
        // 데이터가 늘어날때 y축의 scroll 이 생겨지도록 설정
        graph.getViewport().setScrollableY(true);

        series.setBackgroundColor(Color.parseColor("#4D87cefa"));
        series.setDrawBackground(true);
    }

    public String getFormat(String price) {
        String pm = "";
        if (price.charAt(0) == '-') {
            pm = "- ";
            price = price.substring(1);
        }
        Float temp_price = Float.valueOf(price);
        String res;
        if (temp_price >= 100)
            res = in.format(temp_price);
        else if (temp_price < 100 && temp_price >= 10)
            res = dot1.format(temp_price);
        else
            res = dot2.format(temp_price);
        return pm + res;
    }

    public void addEntry(int x) {
        series.appendData(new DataPoint(cnt++, x), true, 10);
    }

    class NetworkThread extends Thread {
        @Override
        public void run() {
            Api_Client api = new Api_Client("a10c1f984334fb14c30ebaf3e60ce998",
                    "32fe2aae6de50ec84b0ed4cf6093a95b");
            HashMap<String, String> rgParams = new HashMap<String, String>();
            rgParams = new HashMap<String, String>();
            int isFirst = 1;
            Intent intent = getIntent();
            final String search_coin = intent.getExtras().getString("coin_type");
            while (true) {
                if (isFirst == 1) {
                    try {
                        rgParams.put("count", "30");

                        //bithumb 거래소 거래 체결 완료 내역 요청하기
                        String result = api.callApi("/public/transaction_history/" + search_coin + "/KRW", rgParams);
                        JSONObject obj = new JSONObject(result);
                        String status = obj.getString("status");
                        JSONArray data_list = obj.getJSONArray("data");
                        for (int i = 0; i < data_list.length(); i++) {
                            JSONObject data_list_obj = data_list.getJSONObject(i);
                            String transaction_date = data_list_obj.getString("transaction_date");
                            final int price = Integer.parseInt(data_list_obj.getString("price"));
                            price_list.add(price);
                        }
                        drawLineGraph();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    isFirst = 0;
                } else {
                    try {
                        rgParams.put("count", "1");
                        String result = api.callApi("/public/transaction_history/" + coin_name + "/KRW", rgParams);
                        JSONObject obj = new JSONObject(result);
                        String status = obj.getString("status");
                        JSONArray data_list = obj.getJSONArray("data");
                        for (int i = 0; i < data_list.length(); i++) {
                            JSONObject data_list_obj = data_list.getJSONObject(i);
                            String transaction_date = data_list_obj.getString("transaction_date");
                            final int price = Integer.parseInt(data_list_obj.getString("price"));

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    addEntry(price);
                                }
                            });
                            final String res2 = api.callApi("/public/ticker/" + search_coin + "/KRW", rgParams);
                            JSONObject object2 = new JSONObject(res2);
                            JSONObject dt_list = object2.getJSONObject("data");
                            String closing_price = dt_list.getString("closing_price");
                            String fluctate_24H = dt_list.getString("fluctate_24H");
                            String fluctate_rate_24H = dt_list.getString("fluctate_rate_24H");
                            current_price = (TextView) findViewById(R.id.market_price);
                            percentage = (TextView) findViewById(R.id.fluctate_rate);
                            percentage_price = (TextView) findViewById(R.id.fluctate_price);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tv_CoinName.setText(coin_name);
                                    tv_CoinType.setText(coin_type);
                                    iv_coinimg.setImageResource(coin_img);
                                    current_price.setText(getFormat(closing_price));
                                    percentage.setText(getFormat(fluctate_rate_24H));
                                    percentage_price.setText(getFormat(fluctate_24H));
                                    if (fluctate_24H.charAt(0) == '-') {
                                        current_price.setTextColor(Color.BLUE);
                                        percentage_price.setTextColor(Color.BLUE);
                                        percentage.setTextColor(Color.BLUE);
                                    } else {
                                        if (Float.valueOf(fluctate_24H) > 0) {
                                            current_price.setTextColor(Color.RED);
                                            percentage_price.setTextColor(Color.RED);
                                            percentage.setTextColor(Color.RED);
                                        } else {
                                            current_price.setTextColor(Color.BLACK);
                                            percentage_price.setTextColor(Color.BLACK);
                                            percentage.setTextColor(Color.BLACK);
                                        }
                                    }
                                }
                            });
                            sleep(1000);
                        }

                    } catch (Exception e) {
                        Log.d("/////////", "DataGetError");
//                        e.printStackTrace();
                    }
                }

            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
