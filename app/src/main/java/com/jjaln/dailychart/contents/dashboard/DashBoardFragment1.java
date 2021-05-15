package com.jjaln.dailychart.contents.dashboard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.jjaln.dailychart.R;
import com.makeramen.roundedimageview.RoundedImageView;

import lombok.SneakyThrows;

import static android.content.Context.MODE_PRIVATE;

public class DashBoardFragment1 extends Fragment {
    private TextView tvDashName, tvDashUsername;
    private RoundedImageView rivDashboardUser, rivUser;

    private AppCompatButton btnEditProfile;

    private Context mContext;
    private FirebaseUser user;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private String token, username;


    public DashBoardFragment1(RoundedImageView rivUser) {
        this.rivUser = rivUser;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.dashboard_frag_1, container, false );
        mContext = getContext();


        rivDashboardUser = view.findViewById(R.id.riv_dashboard_user);
        tvDashName = view.findViewById(R.id.tv_dash_name);
        tvDashUsername = view.findViewById(R.id.tv_dash_username);
        btnEditProfile = view.findViewById(R.id.btn_edit_profile);


        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        pref = mContext.getSharedPreferences("pref", MODE_PRIVATE);
        token = pref.getString("token", "");
        username = pref.getString("username", "");
        user = FirebaseAuth.getInstance().getCurrentUser();
        tvDashName.setText(user.getDisplayName());
        tvDashUsername.setText(username);
        Glide
                .with(mContext)
                .load(user.getPhotoUrl())
                .centerCrop()
                .placeholder(R.drawable.ic_user)
                .into(rivDashboardUser);

        btnEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), EditProfileActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("username", user.getDisplayName());
            intent.putExtra("nickname", username);
            v.getContext().startActivity(intent);
        });
    }

}
