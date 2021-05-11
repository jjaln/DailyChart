package com.jjaln.dailychart.contents.community;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jjaln.dailychart.R;
import com.jjaln.dailychart.adapter.CommunityContentAdapter;
import com.jjaln.dailychart.feature.Community_Data;
import com.jjaln.dailychart.feature.Contents;
import com.jjaln.dailychart.feature.Reply;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class CommunityContentsActivity extends AppCompatActivity {
    private static final String TAG = "CommunityDetailActivity";
    private Context mContext = CommunityContentsActivity.this;
    private ImageView ivBack, ivSendReply, ivDelete_question;
    private TextView tvToolbarTitle;
    private RecyclerView rvCommunityDetail;
    private EditText etReply;
    private RelativeLayout replyBar;
    private EditText et_reply;
    private CommunityContentAdapter communityDetailAdapter;
    private List<Contents> items;
    private List<Reply> replies;
    private String token;
    private DatabaseReference mDB;
    private int pos;
    private LinearLayoutManager manager;
    private Community_Data community;
    private DatabaseReference mReply;
    private DatabaseReference getmReply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_contents);
        Intent intent = getIntent();
        community = (Community_Data) intent.getSerializableExtra("community");
        pos = intent.getExtras().getInt("pos");

        //토큰
        token = getSharedPreferences("pref", MODE_PRIVATE).getString("token", "");

        //댓글 쓰기
        replyBar = (RelativeLayout) findViewById(R.id.reply_bar);
        replyBar.setVisibility(View.INVISIBLE);
        ivSendReply = findViewById(R.id.iv_reply_send);
        et_reply = findViewById(R.id.et_reply);
        //툴바
        ivBack = findViewById(R.id.iv_back);
        ivBack.setOnClickListener(v -> {
            finish();
        });

        delete_question();


        tvToolbarTitle = findViewById(R.id.tv_toolbar_title);
        tvToolbarTitle.setText("Community");

        manager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rvCommunityDetail = findViewById(R.id.rv_community_detail);
//        //리사이클러뷰 아이템
        items = new ArrayList<>();

        mReply = FirebaseDatabase.getInstance().getReference();
        //댓글 쓰기
        ivSendReply.setOnClickListener(v -> {
            Log.d(TAG, "token :" + token);
            Reply reply = new Reply();
            reply.setContent(et_reply.getText().toString());
            reply.setUsername(community.getUsername());
            reply.setToken(token);
            reply.setReply_key(mReply.child("Reply").
                    child(community.getCategoryName()).
                    child(community.getDBKEy()).push().getKey());
            items.add(new Contents(1, reply));
            mReply.child("Reply").
                    child(community.getCategoryName()).
                    child(community.getDBKEy()).
                    child(reply.getReply_key()).setValue(reply);
            shutdownReplyInput();
        });
        //게시글 내용 불러오기
        addCommunityItem();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void showReplyInput() {           //댓글 키보드 쓰기
        RelativeLayout replyBar = (RelativeLayout) findViewById(R.id.reply_bar);
        replyBar.setVisibility(View.VISIBLE);

        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        etReply = (EditText) findViewById(R.id.et_reply);
        etReply.requestFocus();
        imm.showSoftInput(etReply, InputMethodManager.SHOW_IMPLICIT);
    }

    public void shutdownReplyInput() {           //댓글 키보드 닫기
        RelativeLayout replyBar = (RelativeLayout) findViewById(R.id.reply_bar);
        replyBar.setVisibility(View.INVISIBLE);

        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        etReply = (EditText) findViewById(R.id.et_reply);
        etReply.setText("");
        etReply.requestFocus();
        View view = this.getCurrentFocus();
        imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    private void addCommunityItem() {
        getmReply = FirebaseDatabase.getInstance().getReference();

        getmReply.child("Reply").child(community.getCategoryName())
                .child(community.getDBKEy()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                items = new ArrayList<>();
                replies = new ArrayList<>();
                items.add(new Contents(0, community));
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Reply mReply = dataSnapshot.getValue(Reply.class);
                    replies.add(mReply);
                    items.add(new Contents(1, mReply));
                }
                communityDetailAdapter = new CommunityContentAdapter(items, (CommunityContentsActivity) mContext, token);
                rvCommunityDetail.setLayoutManager(manager);
                rvCommunityDetail.setAdapter(communityDetailAdapter);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Log.d(TAG, "onFailure: 실패");
            }
        });
    }

    private void delete_question()
    {
        ivDelete_question = findViewById(R.id.iv_delete_question);
        if (token.equals(community.getToken())) {
            ivDelete_question.setVisibility(View.VISIBLE);
        } else {
            ivDelete_question.setVisibility(View.INVISIBLE);
        }
        mDB = FirebaseDatabase.getInstance().getReference();
        Intent intent1 = new Intent(this, CommunityActivity.class);
        ivDelete_question.setOnClickListener(v ->
        {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Delete");
            alert.setPositiveButton("accept", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mDB.child("Post").
                            child(community.getCategoryName()).
                            child(community.getDBKEy()).removeValue();
                    mDB.child("Reply")
                            .child(community.getCategoryName()).
                            child(community.getDBKEy()).removeValue();
                    intent1.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    finish();
                    startActivity(intent1);
                }
            });
            alert.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                }
            });
            alert.show();
        });
    }
}