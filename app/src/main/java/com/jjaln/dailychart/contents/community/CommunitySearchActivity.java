package com.jjaln.dailychart.contents.community;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjaln.dailychart.R;
import com.jjaln.dailychart.adapter.CommunityAdapter;
import com.jjaln.dailychart.feature.Community_Data;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CommunitySearchActivity extends AppCompatActivity {
    private ImageView ivBack;
    private TextView tvToolbarTitle, etSearch;
    private RecyclerView rvSearchList;
    private CommunityAdapter communityAdapter;
    private List<Community_Data> community;
    private DatabaseReference mDatabase;
    private final Context mContext = CommunitySearchActivity.this;
    private Button bt_search;
    private LinearLayoutManager manager;
    private SharedPreferences pref;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_search);

        ivBack = findViewById(R.id.iv_back);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        pref = getSharedPreferences("pref", MODE_PRIVATE);
        token = pref.getString("token", "");
        tvToolbarTitle = findViewById(R.id.tv_toolbar_title);
        tvToolbarTitle.setText("Search");
        etSearch = findViewById(R.id.et_search);
        bt_search = findViewById(R.id.btn_search);
        ivBack.setOnClickListener(v -> {
            finish();
        });

        rvSearchList = findViewById(R.id.rv_search_list);
        manager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);

        bt_search.setOnClickListener(v -> {
            SearchContents(etSearch.getText().toString());
//            Toast.makeText(this, etSearch.getText().toString(), Toast.LENGTH_SHORT).show();
        });
    }

    public void SearchContents(final String text) {
        mDatabase.child("Search").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                community = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Community_Data data = dataSnapshot.getValue(Community_Data.class);
                    if (data.getTitle().contains(text)) {
                        community.add(0, data);
                        communityAdapter = new CommunityAdapter(community, mContext, token);
                    }
                }
                rvSearchList.setAdapter(communityAdapter);
                rvSearchList.setLayoutManager(manager);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });
    }
}