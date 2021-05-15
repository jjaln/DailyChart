package com.jjaln.dailychart.contents.dashboard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.jjaln.dailychart.R;
import com.google.android.material.textfield.TextInputEditText;

import lombok.SneakyThrows;;

public class EditProfileActivity extends AppCompatActivity {

    private static final String TAG = "EditProfileActivity";

    private ImageView ivBack;
    private TextView tvToolbarTitle;
    private SharedPreferences pref;
    private TextInputEditText tfEditName,tfEditNickName;
    private Button btnEditProfileSave;
    private SharedPreferences.Editor editor;
    private String token,username,nickname;

    private Context mContext;

    @SneakyThrows
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mContext = getApplicationContext();

        loadPref();

        init();


        btnEditProfileSave.setOnClickListener(v -> {
            if(tfEditNickName.getText().toString().equals(""))
                Toast.makeText(mContext,"Check yout input!",Toast.LENGTH_SHORT).show();
            else
            {
                editor=pref.edit();
                editor.putString("username",tfEditNickName.getText().toString());
                editor.commit();
                Toast.makeText(mContext,"Update your nickname!",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void loadPref() throws Exception {
        pref = getSharedPreferences("pref", MODE_PRIVATE);
        token = pref.getString("token", "");
        nickname = pref.getString("username","");
    }

    private void init(){
        ivBack = findViewById(R.id.iv_back);
        tvToolbarTitle = findViewById(R.id.tv_toolbar_title);
        tfEditName = findViewById(R.id.tf_edit_name);
        btnEditProfileSave = findViewById(R.id.btn_edit_profile_save);
        tfEditNickName = findViewById(R.id.tf_edit_nickname);
        // 툴바 title text 설정
        tvToolbarTitle.setText("Edit Profile");

        // 뒤로가기 버튼
        ivBack.setOnClickListener(v -> {
            finish();
        });

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        tfEditName.setText(username);
        tfEditNickName.setText(nickname);

    }
}