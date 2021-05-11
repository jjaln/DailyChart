package com.jjaln.dailychart;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;

import androidx.drawerlayout.widget.DrawerLayout;

import com.jjaln.dailychart.contents.community.CommunityActivity;
import com.jjaln.dailychart.contents.course.CoursesActivity;
import com.jjaln.dailychart.contents.dashboard.UserDashBoardActivity;
import com.jjaln.dailychart.contents.login.LoginActivity;
import com.google.android.material.navigation.NavigationView;

public class NavigationViewHelper {
    private static final String TAG = "NavigationViewHelper";

    public static void enable(Context context, NavigationView view) {
        view.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            DrawerLayout drawer = (DrawerLayout)((Activity) context).findViewById(R.id.drawer);


            if (id == R.id.login) {
                Log.d(TAG, "enable: Login 클릭");
                Intent intent = new Intent(context, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                context.startActivity(intent);
            }
            else if(id == R.id.dashboard){
                Log.d(TAG, "enable: dashboard 클릭");
                Intent intent = new Intent(context, UserDashBoardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                context.startActivity(intent);
            }
            else if (id == R.id.courses) {
                Log.d(TAG, "enable: Courses 클릭");
                Intent intent = new Intent(context, CoursesActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                context.startActivity(intent);
            } else if (id == R.id.community) {
                Log.d(TAG, "enable: Community 클릭");
                Intent intent = new Intent(context, CommunityActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                context.startActivity(intent);
            }
            drawer.closeDrawer(Gravity.LEFT);
            return false;
        });
    }
}
