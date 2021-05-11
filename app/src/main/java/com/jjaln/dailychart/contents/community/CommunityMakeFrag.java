package com.jjaln.dailychart.contents.community;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jjaln.dailychart.R;
import com.jjaln.dailychart.adapter.CommunityAdapter;
import com.jjaln.dailychart.feature.Community_Data;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class CommunityMakeFrag extends Fragment {

    private RecyclerView rvCommunityNew;
    private Context mContext;
    private static final String TAG = "CommunityFragAll";
    private List<Community_Data> communities = new ArrayList<>();
    private CommunityAdapter communityAdapter;
    private String token,sort,name;
    private MaterialButtonToggleGroup materialButtonToggleGroup;
    private int page;
    private LinearLayoutManager manager;
    private ProgressBar progressBar;
    public CommunityMakeFrag(String sort, String token, String categoryName){
        this.sort=sort;
        this.token=token;
        this.name = categoryName;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.community_make_fag, container, false);

        mContext = container.getContext();

        progressBar = view.findViewById(R.id.progressbar);
        progressBar.setVisibility(View.INVISIBLE);

        manager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        rvCommunityNew = view.findViewById(R.id.rv_community_all);

        addFrag();
        return view;
    }



    private void addFrag(){
        page=0;
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Post").child(name).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                communities = new ArrayList<>();
                for(DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    Community_Data data = dataSnapshot.getValue(Community_Data.class);
                    communities.add(0,data);
                    communityAdapter = new CommunityAdapter(communities,mContext,token);
                }
                rvCommunityNew.setAdapter(communityAdapter);
                rvCommunityNew.setLayoutManager(manager);
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Log.d(TAG, "onFailure: 실패");
            }
        });
    }
}