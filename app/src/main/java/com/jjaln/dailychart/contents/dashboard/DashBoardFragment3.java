package com.jjaln.dailychart.contents.dashboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jjaln.dailychart.R;

public class DashBoardFragment3 extends Fragment {
    private SharedPreferences pref;
    private Context mContext;
    private String token;
    private long userId;

    private LinearLayout layoutNoList,layoutPayTable;

    private RecyclerView rvPaymentHistory;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.dashboard_frag_3, container, false );
        mContext = getContext();

        layoutNoList = view.findViewById(R.id.layout_no_list);
        layoutPayTable =view.findViewById(R.id.layout_pay_table);

        rvPaymentHistory = view.findViewById(R.id.rv_payment_history);
        rvPaymentHistory.setLayoutManager(new LinearLayoutManager(mContext));

        return view;
    }
}
