package com.jjaln.dailychart;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Gravity;

import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.jjaln.dailychart.contents.about.AboutActivity;
import com.jjaln.dailychart.contents.community.CommunityActivity;
import com.jjaln.dailychart.contents.premium.PremiumActivity;
import com.jjaln.dailychart.contents.dashboard.UserDashBoardActivity;
import com.jjaln.dailychart.contents.login.LoginActivity;
import com.google.android.material.navigation.NavigationView;
import com.jjaln.dailychart.contents.setting.SettingActivity;

import static android.content.Context.MODE_PRIVATE;

public class NavigationViewHelper {
    private static final String TAG = "NavigationViewHelper";

    private static GoogleSignInClient googleSignInClient;
    public static void enable(Context context, NavigationView view) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(context, gso);
        view.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            DrawerLayout drawer = (DrawerLayout)((Activity) context).findViewById(R.id.drawer);

             if(id == R.id.logout){
                SharedPreferences pref = context.getSharedPreferences("pref", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("token", "");
                editor.putString("username", "");
                editor.commit();
                FirebaseAuth.getInstance().signOut();
                googleSignInClient.signOut();
                Intent intent = new Intent(context, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                context.startActivity(intent);
                ((Activity) context).finish();
            }
            else if(id == R.id.dashboard){
                Log.d(TAG, "enable: dashboard 클릭");
                Intent intent = new Intent(context, UserDashBoardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                context.startActivity(intent);
            }
            else if (id == R.id.premium) {
                Log.d(TAG, "enable: Premium 클릭");
                Intent intent = new Intent(context, PremiumActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                context.startActivity(intent);
            } else if (id == R.id.community) {
                Log.d(TAG, "enable: Community 클릭");
                Intent intent = new Intent(context, CommunityActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                context.startActivity(intent);
            }
            else if (id == R.id.about) {
                Log.d(TAG, "enable: about 클릭");
                Intent intent = new Intent(context, AboutActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                context.startActivity(intent);
            }
             else if (id == R.id.setting) {
                 Log.d(TAG, "enable: setting 클릭");
                 Intent intent = new Intent(context, SettingActivity.class);
                 intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                 context.startActivity(intent);
             }
            drawer.closeDrawer(Gravity.LEFT);
            return false;
        });
    }
}
