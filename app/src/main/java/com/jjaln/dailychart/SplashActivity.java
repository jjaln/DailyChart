package com.jjaln.dailychart;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.jjaln.dailychart.feature.Coin;

import java.util.ArrayList;

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
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
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