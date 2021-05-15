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
        pref = getSharedPreferences("pref",MODE_PRIVATE);

        tvToolbarTitle = findViewById(R.id.tv_toolbar_title);
        tvToolbarTitle.setText("DashBoard");

        ivBack.setOnClickListener(v -> {
            finish();
        });

        rivDashboardUser = findViewById(R.id.riv_dashboard_user);

        rivUser = (RoundedImageView) findViewById(R.id.riv_user);
        rivUserClick();

        vpContainer = findViewById(R.id.vp_container);
        tabsDashboard = findViewById(R.id.tabs_dashboard);

        dashboardFragmentAdapter = new DashBoardFragmentAdapter(getSupportFragmentManager(),1);

        dashboardFragmentAdapter.addFragment(new DashBoardFragment1(rivUser));
        dashboardFragmentAdapter.addFragment(new DashBoardFragment2());
        dashboardFragmentAdapter.addFragment(new DashBoardFragment3());

        vpContainer.setAdapter(dashboardFragmentAdapter);

        tabsDashboard.setupWithViewPager(vpContainer);

        tabsDashboard.getTabAt(0).setText("My Profile");
        tabsDashboard.getTabAt(1).setText("My Courses");
        tabsDashboard.getTabAt(2).setText("My Payment History");
    }
    private void rivUserClick(){
        rivUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu p = new PopupMenu(
                        getApplicationContext(),//화면제어권자
                        v);             //팝업을 띄울 기준이될 위젯
                getMenuInflater().inflate(R.menu.user_menu, p.getMenu());
                //이벤트 처리
                p.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getTitle().equals("Dashboard")) {
                            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(v.getContext(), UserDashBoardActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            v.getContext().startActivity(intent);

                        } else {
                            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("token", "");
                            editor.commit();
                            Intent intent = new Intent(v.getContext(), MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            v.getContext().startActivity(intent);
                            finish();

                        }
                        return false;
                    }
                });
                p.show();
            }
        });
    }
}