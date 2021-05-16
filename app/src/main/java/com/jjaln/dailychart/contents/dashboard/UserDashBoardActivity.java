package com.jjaln.dailychart.contents.dashboard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.jjaln.dailychart.MainActivity;
import com.jjaln.dailychart.R;
import com.google.android.material.tabs.TabLayout;
import com.makeramen.roundedimageview.RoundedImageView;

public class UserDashBoardActivity extends AppCompatActivity {

    private ImageView ivBack;
    private TextView tvToolbarTitle, tvDashName, tvDashUsername;
    private RoundedImageView rivUser, rivDashboardUser;
    private RecyclerView rvDashTech;
    private SharedPreferences pref;
    private DashBoardFragmentAdapter dashboardFragmentAdapter;
    private ViewPager vpContainer;
    private TabLayout tabsDashboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dash_board);

        ivBack = findViewById(R.id.iv_back);
        pref = getSharedPreferences("pref", MODE_PRIVATE);

        tvToolbarTitle = findViewById(R.id.tv_toolbar_title);
        tvToolbarTitle.setText("DashBoard");

        ivBack.setOnClickListener(v -> {
            finish();
        });

        rivDashboardUser = findViewById(R.id.riv_dashboard_user);

        vpContainer = findViewById(R.id.vp_container);
        tabsDashboard = findViewById(R.id.tabs_dashboard);

        dashboardFragmentAdapter = new DashBoardFragmentAdapter(getSupportFragmentManager(), 1);

        dashboardFragmentAdapter.addFragment(new UserInfoFragment(rivUser));
        dashboardFragmentAdapter.addFragment(new CommunityFragment());
        dashboardFragmentAdapter.addFragment(new DashBoardFragment3());

        vpContainer.setAdapter(dashboardFragmentAdapter);

        tabsDashboard.setupWithViewPager(vpContainer);

        tabsDashboard.getTabAt(0).setText("My Profile");
        tabsDashboard.getTabAt(1).setText("My Post");
        tabsDashboard.getTabAt(2).setText("My Payment History");
    }
}
