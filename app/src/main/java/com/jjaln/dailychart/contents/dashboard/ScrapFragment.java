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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjaln.dailychart.MainActivity;
import com.jjaln.dailychart.R;
import com.jjaln.dailychart.adapter.CommunityListAdapter;
import com.jjaln.dailychart.adapter.NewScrapAdapter;
import com.jjaln.dailychart.contents.community.CommunityActivity;
import com.jjaln.dailychart.feature.Community_Data;
import com.jjaln.dailychart.feature.NewsCrap;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import lombok.SneakyThrows;

import static android.content.Context.MODE_PRIVATE;

public class ScrapFragment extends Fragment {
    private static final String TAG = "DashboardFragment2";
    private SharedPreferences pref;
    private Context mContext;
    private String token;
    private NewScrapAdapter newScrapAdapter;
    private List<NewsCrap> newsCraps;
    private DatabaseReference mDatabase;
    private AppCompatButton btnFindOne;
    private RelativeLayout layoutNoList;
    private RecyclerView rvMyPost;
    private LinearLayoutManager manager;

    @SneakyThrows
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dashboard_newscrap, container, false);
        mContext = getContext();

        manager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
        rvMyPost = view.findViewById(R.id.rv_my_post);
        rvMyPost.setLayoutManager(manager);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        pref = getActivity().getSharedPreferences("pref", MODE_PRIVATE);
        token = pref.getString("token", "");

        btnFindOne = view.findViewById(R.id.btn_find_one);
        btnFindOne.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
            getActivity().finish();
        });

        layoutNoList = view.findViewById(R.id.layout_no_list);
        NewScrap();

        return view;
    }

    public void NewScrap() {
        mDatabase.child("Scrap").child(token).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                newsCraps = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    NewsCrap data = dataSnapshot.getValue(NewsCrap.class);
                    layoutNoList.setVisibility(View.GONE);
                    newsCraps.add(0, data);
                    newScrapAdapter = new NewScrapAdapter(newsCraps, mContext);
                }
                rvMyPost.setAdapter(newScrapAdapter);
                rvMyPost.setLayoutManager(manager);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });
    }
}
