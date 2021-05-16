package com.jjaln.dailychart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jjaln.dailychart.adapter.CoinListAdapter;
import com.jjaln.dailychart.feature.Coin;
import com.jjaln.dailychart.wallet.Api_Client;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

public class SplashActivity extends AppCompatActivity {
    public ArrayList<Coin> CoinData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        CoinData = new ArrayList<>();
        CoinData.add(new Coin(R.mipmap.btc, "BTC", "0", "0", "0"));
        CoinData.add(new Coin(R.mipmap.eth, "ETH", "0", "0", "0"));
        CoinData.add(new Coin(R.mipmap.xrp, "XRP", "0", "0", "0"));
        CoinData.add(new Coin(R.mipmap.ada, "ADA", "0", "0", "0"));
        CoinData.add(new Coin(R.mipmap.dot, "DOT", "0", "0", "0"));
        startLoading();
    }
    private void startLoading()
    {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("init",CoinData);
                startActivity(intent);
                finish();
            }
        },1500);
    }
    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}