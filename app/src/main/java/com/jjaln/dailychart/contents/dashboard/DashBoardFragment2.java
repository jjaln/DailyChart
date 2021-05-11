package com.jjaln.dailychart.contents.dashboard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jjaln.dailychart.R;
import com.jjaln.dailychart.contents.course.CoursesActivity;

import lombok.SneakyThrows;

public class DashBoardFragment2 extends Fragment {
    private static final String TAG = "DashboardFragment2";
    private SharedPreferences pref;
    private Context mContext;
    private String token;
    private long userId;
    private AppCompatButton btnFindOne;
    private RelativeLayout layoutNoList;
    private RecyclerView rvMyCourses;
    @SneakyThrows
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.dashboard_frag_2, container, false );
        mContext = getContext();


        LinearLayoutManager manager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
        rvMyCourses = view.findViewById(R.id.rv_my_courses);
        rvMyCourses.setLayoutManager(manager);


        btnFindOne = view.findViewById(R.id.btn_find_one);
        btnFindOne.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), CoursesActivity.class);
            startActivity(intent);
            getActivity().finish();
        });

        layoutNoList = view.findViewById(R.id.layout_no_list);


        return view;
    }
}
