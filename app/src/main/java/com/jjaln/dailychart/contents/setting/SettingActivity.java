package com.jjaln.dailychart.contents.setting;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.jjaln.dailychart.R;

import static com.jjaln.dailychart.contents.setting.SettingPreference.isMessage;

public class SettingActivity extends AppCompatActivity {
    TextView textView;
    TextView textView2;
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        SharedPreferences sharedPreferences = getSharedPreferences("setting", MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit(); //sharedPreferences를 제어할 editor를 선언
        editor.putBoolean("isMessage",isMessage); // key,value 형식으로 저장
        editor.commit();    //최종 커밋. 커밋을 해야 저장이 된다.



    }


}
