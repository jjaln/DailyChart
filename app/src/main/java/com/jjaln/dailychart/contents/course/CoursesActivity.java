package com.jjaln.dailychart.contents.course;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jjaln.dailychart.R;
import com.makeramen.roundedimageview.RoundedImageView;

public class CoursesActivity extends AppCompatActivity {

    private ImageView ivBack;
    private TextView tvToolbarTitle;
    private RecyclerView rvChallengeList;
    private RoundedImageView rivUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);

        ivBack = findViewById(R.id.iv_back);

        tvToolbarTitle = findViewById(R.id.tv_toolbar_title);

        ivBack.setOnClickListener(v -> {
            finish();
        });

        LinearLayoutManager manager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rvChallengeList = findViewById(R.id.rv_course_list);

    }
}