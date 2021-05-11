package com.jjaln.dailychart.contents.community;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.jjaln.dailychart.R;

public class CommunitySearchActivity extends AppCompatActivity {
    private ImageView ivBack;
    private TextView tvToolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_search);

        ivBack = findViewById(R.id.iv_back);

        tvToolbarTitle = findViewById(R.id.tv_toolbar_title);
        tvToolbarTitle.setText("검색");

        ivBack.setOnClickListener(v -> {
            finish();
        });
    }
}