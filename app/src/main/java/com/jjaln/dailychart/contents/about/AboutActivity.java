package com.jjaln.dailychart.contents.about;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.jjaln.dailychart.R;

public class AboutActivity extends AppCompatActivity {

    private ImageView ivBack;
    private TextView tvToolbarTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        ivBack = findViewById(R.id.iv_back);

        tvToolbarTitle = findViewById(R.id.tv_toolbar_title);
        tvToolbarTitle.setText("About us");
        ivBack.setOnClickListener(v -> {
            finish();
        });

    }
}