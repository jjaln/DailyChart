package com.jjaln.dailychart.contents.dashboard;

import android.content.Context;
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

import com.jjaln.dailychart.R;
import com.makeramen.roundedimageview.RoundedImageView;

import lombok.SneakyThrows;

public class DashBoardFragment1 extends Fragment {
    private TextView tvDashName, tvDashUsername;
    private RoundedImageView rivDashboardUser, rivUser;

    private AppCompatButton btnEditProfile;
    private RecyclerView rvDashTech;
    private Context mContext;

    public DashBoardFragment1(RoundedImageView rivUser) {
        this.rivUser = rivUser;
    }
    @SneakyThrows
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.dashboard_frag_1, container, false );
        mContext = getContext();


        rivDashboardUser = view.findViewById(R.id.riv_dashboard_user);
        tvDashName = view.findViewById(R.id.tv_dash_name);
        tvDashUsername = view.findViewById(R.id.tv_dash_username);
        rvDashTech = view.findViewById(R.id.rv_dash_tech);
        btnEditProfile = view.findViewById(R.id.btn_edit_profile);


        return view;
    }
}
