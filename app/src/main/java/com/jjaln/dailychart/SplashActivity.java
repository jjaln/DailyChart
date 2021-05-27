package com.jjaln.dailychart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.jjaln.dailychart.contents.login.LoginActivity;
import com.jjaln.dailychart.feature.Coin;

import java.util.ArrayList;

public class SplashActivity extends AppCompatActivity {
    private SharedPreferences pref;
    private String token="";
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        pref = getSharedPreferences("pref", MODE_PRIVATE);
        token = pref.getString("token", "");
        startLoading();
    }
    private void startLoading()
    {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(token.equals(""))
                    intent = new Intent(getApplicationContext(), LoginActivity.class);
                else
                    intent = new Intent(getApplicationContext(), MainActivity.class);
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