package com.jjaln.dailychart;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jjaln.dailychart.adapter.CoinListAdapter;
import com.jjaln.dailychart.adapter.ExchangeAdapter;
import com.jjaln.dailychart.contents.dashboard.UserDashBoardActivity;
import com.jjaln.dailychart.feature.Coin;
import com.jjaln.dailychart.feature.Exchange;
import com.jjaln.dailychart.notification.MyService;
import com.jjaln.dailychart.wallet.Api_Client;
import com.makeramen.roundedimageview.RoundedImageView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

import lombok.SneakyThrows;

import static com.jjaln.dailychart.notification.MyService.aLive;

public class MainActivity extends AppCompatActivity {

    private Context mContext = MainActivity.this;

    private Toolbar toolbarNomad;
    private ImageView ivMenu, ivApi;
    private DrawerLayout drawer;
    private NavigationView nv;
    private CoinListAdapter coinAdapter;
    private LinearLayoutManager coinManager;
    private TextView tv_currentAsset, tv_currentAsset2;
    private RecyclerView rvCoin, rvExchange;
    private SharedPreferences pref;
    private String token;
    private ArrayList<Coin> CoinData;
    private FirebaseUser user;
    private RoundedImageView rivUser;
    private static final String TAG = "MainActivity";
    private  Intent serviceIntent;
    public static boolean isBackground;
    String bithumb_access;
    String bithumb_secret;
    String upbit_access;
    String upbit_secret;
    NetworkThread thread;
    Handler handler = new Handler();
    DecimalFormat form = new DecimalFormat("#.####");

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("/////////", "oncreate start");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        toolbarNomad = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbarNomad);

        ivMenu = findViewById(R.id.iv_back);
        tv_currentAsset = findViewById(R.id.tv_currentAsset);
        tv_currentAsset2 = findViewById(R.id.tv_currentAsset2);
        ivApi = findViewById(R.id.apibutton);
        drawer = findViewById(R.id.drawer);
        rivUser = (RoundedImageView) findViewById(R.id.riv_user);

        ivMenu.setOnClickListener(v -> {
            drawer.openDrawer(Gravity.LEFT);
        });

        ivApi.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), APIActivity.class);
            thread.interrupt();
            startActivityForResult(intent, 1);
        });

        LayoutInflater mInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(R.layout.navigation, drawer, true);
        nv = findViewById(R.id.nv);

        com.jjaln.dailychart.NavigationViewHelper.enable(MainActivity.this, nv);

        //Exchage List
        rvExchange = findViewById(R.id.rv_exchangeList);
        ArrayList<Exchange> ExchangeData = new ArrayList<>();

        ExchangeData.add(new Exchange(R.mipmap.bithumb, "com.btckorea.bithumb", "Bithumb"));
        ExchangeData.add(new Exchange(R.mipmap.upbit, "com.dunamu.exchange", "Upbit"));

        LinearLayoutManager exchange_manager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        rvExchange.setLayoutManager(exchange_manager);
        rvExchange.setAdapter(new ExchangeAdapter(ExchangeData));

        rvCoin = findViewById(R.id.rv_CoinList);
        Log.d("/////////", "oncreate End");
        thread = new NetworkThread();
        thread.start();


        // ???????????? ?????? ??????
        /*PowerManager pm = (PowerManager) getApplicationContext().getSystemService(POWER_SERVICE);
        boolean isWhiteListing = false;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            isWhiteListing = pm.isIgnoringBatteryOptimizations(getApplicationContext().getPackageName());
        }
        if (!isWhiteListing) {
            Intent pmintent = new Intent();
            pmintent.setAction(android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            pmintent.setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
            startActivity(pmintent);
        }*/

        aLive = true;

        // ?????? ?????? ??????????????? ??? ?????? ??????
        if (MyService.serviceIntent == null) {
            // ??????????????? ????????? ??????
            Log.d("background thread", "start");
            Intent service_intent = new Intent(MainActivity.this, MyService.class);
            startService(service_intent);
        }else{
            serviceIntent = MyService.serviceIntent;//getInstance().getApplication();
            //Toast.makeText(getApplicationContext(), "already", Toast.LENGTH_LONG).show();
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

//        isforeground = true;
        // ??? ????????? ?????????(MyService) ??????(stopService)
        if (serviceIntent!=null) {
            stopService(serviceIntent);
            serviceIntent = null;
        }

        Intent service_intent = new Intent(MainActivity.this, MyService.class);
        stopService(service_intent);
        startService(service_intent);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_CANCELED) {
            Bundle bundle = data.getBundleExtra("apikey");
            bithumb_access = bundle.getString("bit_acc");
            bithumb_secret = bundle.getString("bit_sec");
            upbit_access = bundle.getString("up_acc");
            upbit_secret = bundle.getString("up_sec");
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        aLive = true;
        coinManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
        rvCoin.setLayoutManager(coinManager);
        pref = getSharedPreferences("pref", MODE_PRIVATE);
        token = pref.getString("token", "");
        UserIcon();
        Log.d("/////////", "OnResume End");
    }

    @Override
    protected void onStop() {
        super.onStop();
//        isforeground = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
//        isforeground = true;
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
//        isforeground = true;
    }

    private void UserIcon() {
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
                Intent intent = new Intent(v.getContext(), UserDashBoardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    class NetworkThread extends Thread {
        @SneakyThrows
        @Override
        public void run() {
            while (true) {

                if (bithumb_access != null && bithumb_secret != null) {
                    Api_Client api = new Api_Client(""+bithumb_access,
                            ""+bithumb_secret);
                    HashMap<String, String> rgParams = new HashMap<String, String>();
                    rgParams.put("currency", "ALL");

                    double balance = 0.0;

                    final String result = api.callApi("/info/balance", rgParams);

                    JSONObject obj = new JSONObject(result);
                    String status = obj.getString("status");

                    JSONObject data_list = obj.getJSONObject("data");
                    String total_krw = data_list.getString("total_krw");
                    String total_btc = data_list.getString("total_btc");
                    String total_eth = data_list.getString("total_eth");
                    String total_xrp = data_list.getString("total_xrp");
                    String total_dot = data_list.getString("total_dot");
                    String total_ada = data_list.getString("total_ada");
                    String xcoin_last_btc = data_list.getString("xcoin_last_btc");
                    String xcoin_last_eth = data_list.getString("xcoin_last_eth");
                    String xcoin_last_xrp = data_list.getString("xcoin_last_xrp");
                    String xcoin_last_dot = data_list.getString("xcoin_last_dot");
                    String xcoin_last_ada = data_list.getString("xcoin_last_ada");


                    balance = Float.parseFloat(total_krw) + (Float.parseFloat(xcoin_last_btc) * Float.parseFloat(total_btc)) +
                            (Float.parseFloat(xcoin_last_eth) * Float.parseFloat(total_eth)) +
                            (Float.parseFloat(xcoin_last_xrp) * Float.parseFloat(total_xrp)) +
                            (Float.parseFloat(xcoin_last_ada) * Float.parseFloat(total_ada)) +
                            (Float.parseFloat(xcoin_last_dot) * Float.parseFloat(total_dot));
                    System.out.println(balance);
                    String Asset = "Bithumb : " + form.format(balance);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv_currentAsset.setText(Asset);

                        }
                    });
                }

                if (upbit_access != null && upbit_secret != null) {
                    String accessKey = (upbit_access);
                    String secretKey = (upbit_secret);
                    String serverUrl = ("https://api.upbit.com");

                    Algorithm algorithm = Algorithm.HMAC256(secretKey);
                    String jwtToken = JWT.create()
                            .withClaim("access_key", accessKey)
                            .withClaim("nonce", UUID.randomUUID().toString())
                            .sign(algorithm);

                    String authenticationToken = "Bearer " + jwtToken;
                    HttpClient client = HttpClientBuilder.create().build();
                    HttpGet request = new HttpGet(serverUrl + "/v1/accounts");
                    request.setHeader("Content-Type", "application/json");
                    request.addHeader("Authorization", authenticationToken);

                    HttpResponse response = client.execute(request);
                    HttpEntity entity = response.getEntity();

                    double balance_total = 0.0;
                    JsonParser jsonParser = new JsonParser();
                    JsonArray jsonArray = (JsonArray) jsonParser.parse(EntityUtils.toString(entity, "UTF-8"));
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JsonObject object = (JsonObject) jsonArray.get(i);
                        String code = object.get("currency").getAsString();
                        if (code.equals("KRW") || code.equals("BTC") || code.equals("ETH") || code.equals("XRP")
                                || code.equals("ADA") || code.equals("DOT")) {
                            Double balances = object.get("balance").getAsDouble();
                            balance_total += balances;
                        }

                        String Asset2 = "Upbit : " + form.format(balance_total);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tv_currentAsset2.setText(Asset2);
                            }
                        });
                    }
                }

                Api_Client api = new Api_Client(""+bithumb_access,
                        ""+bithumb_secret);
                HashMap<String, String> rgParams = new HashMap<String, String>();
                rgParams.put("currency", "ALL");
                String[] coin_type = {"BTC", "ETH", "XRP", "ADA", "DOT"};
                String[] coin_names = {"Bitcoin","Ethereum","Ripple","ADA","Polkadot"};
                ArrayList<String> coins = new ArrayList<>(Arrays.asList(coin_type));
                int[] coin_img = {R.mipmap.btc, R.mipmap.eth, R.mipmap.xrp, R.mipmap.ada, R.mipmap.dot};

                CoinData = new ArrayList<>();
                coinAdapter = new CoinListAdapter(CoinData, mContext);
                for (String type : coin_type) {
                    final String res = api.callApi("/public/ticker/" + type + "/KRW", rgParams);
                    JSONObject object = new JSONObject(res);
                    JSONObject dt_list = object.getJSONObject("data");
                    int c_img = coin_img[coins.indexOf(type)];
                    String coin_name = coin_names[coins.indexOf(type)];
                    String closing_price = dt_list.getString("closing_price");
                    String fluctate_24H = dt_list.getString("fluctate_24H");
                    String fluctate_rate_24H = dt_list.getString("fluctate_rate_24H");
                    CoinData.add(new Coin(c_img,coin_name, type, closing_price, fluctate_rate_24H, fluctate_24H));
//                    Log.d("//////////", closing_price + " / " + fluctate_24H);
                }
                coinAdapter = new CoinListAdapter(CoinData, mContext);
                Log.d("/////////", "Thread End");
                try {
                    Thread.sleep(1000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            rvCoin.setAdapter(coinAdapter);
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}

