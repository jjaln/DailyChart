package com.jjaln.dailychart.contents.community;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.jjaln.dailychart.R;
import com.jjaln.dailychart.adapter.CommunityPagerAdapter;
import com.jjaln.dailychart.contents.login.LoginActivity;
import com.jjaln.dailychart.feature.Category;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class CommunityActivity extends AppCompatActivity {

    public static List<String> category;
    private ImageView ivBack, ivWrite, ivSearch;
    private TextView tvToolbarTitle;
    private ViewPager vpContainer;
    private TabLayout tab_community;
    private CommunityPagerAdapter communityPagerAdapter;
    private List<Category> comCategoryList;
    private String token;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ivBack = findViewById(R.id.iv_back);

        category = new ArrayList<>(Arrays.asList(new String[]{"Bitcoin", "Ethereum", "Ripple", "Ada","Polkadot"}));
        comCategoryList = new ArrayList<>();
        for (int i = 0; i < category.size(); i++)
            comCategoryList.add(new Category(i + 1, category.get(i)));

        tvToolbarTitle = findViewById(R.id.tv_toolbar_title);
        tvToolbarTitle.setText("커뮤니티");

        pref = getSharedPreferences("pref", MODE_PRIVATE);
        token = pref.getString("token", "");

        ivBack.setOnClickListener(v -> {
            finish();
        });

        ivWrite = findViewById(R.id.iv_write);

        ivWrite.setOnClickListener(v -> {
            Intent intent = new Intent();
            if(token.equals(""))
            {
                intent = new Intent(this, LoginActivity.class);
            }
            else{
                intent = new Intent(this, CommunityWriteActivity.class);
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        ivSearch = findViewById(R.id.iv_search);

        ivSearch.setOnClickListener(v -> {
            Intent intent = new Intent(this, CommunitySearchActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });


        vpContainer = findViewById(R.id.vp_container);
        tab_community = findViewById(R.id.tabs_community);

        communityPagerAdapter = new CommunityPagerAdapter(getSupportFragmentManager(), 1);

        for (int i = 0; i < comCategoryList.size(); i++) {
            communityPagerAdapter.addFragment(new CommunityMakeFrag("new", token, comCategoryList.get(i).getTitle()));
        }

        vpContainer.setAdapter(communityPagerAdapter);
        tab_community.setupWithViewPager(vpContainer);

        for (int i = 0; i < comCategoryList.size(); i++) {
            Objects.requireNonNull(tab_community.getTabAt(i)).setText("# " + comCategoryList.get(i).getTitle());
        }
    }
}