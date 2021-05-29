package com.jjaln.dailychart.adapter;

import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jjaln.dailychart.R;
import com.jjaln.dailychart.contents.community.CommunityContentsActivity;
import com.jjaln.dailychart.contents.community.RichEditor.RichEditor;
import com.jjaln.dailychart.feature.Community_Data;
import com.jjaln.dailychart.feature.Contents;
import com.jjaln.dailychart.feature.Reply;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;


public class CommunityContentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Contents> items;
    private CommunityContentsActivity communityDetailActivity;
    private String token;
    private String Q_category, Q_DBkey;
    private long id;
    private static final String TAG = "CommunityDetailAdapter:";

    public CommunityContentAdapter(List<Contents> items, CommunityContentsActivity communityDetailActivity, String token) {
        this.items = items;
        this.communityDetailActivity = communityDetailActivity;
        this.token = token;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new DetailContentViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(
                            R.layout.community_content_question,
                            parent,
                            false
                    )
            );
        } else {
            return new ReplyViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(
                            R.layout.community_content_reply,
                            parent,
                            false
                    )
            );
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: " + position);
        if (getItemViewType(position) == 0) {
            Community_Data community = (Community_Data) items.get(position).getObject();
            Q_category = community.getCategoryName();
            Q_DBkey = community.getDBKEy();
            ((DetailContentViewHolder) holder).setItem(community);
        }
        else if (getItemViewType(position) == 1) {
            Reply reply = (Reply) items.get(position).getObject();
            ((ReplyViewHolder) holder).setItem(reply);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getType();
    }

    public class DetailContentViewHolder extends RecyclerView.ViewHolder {

        private TextView tvDetailTitle, tvDetailUsername, tvDetailCategory, tvDetailDate;
        private RichEditor tvDetailContents;

        public DetailContentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDetailTitle = itemView.findViewById(R.id.tv_detail_title);
            tvDetailUsername = itemView.findViewById(R.id.tv_detail_username);
            tvDetailCategory = itemView.findViewById(R.id.tv_detail_category);
            tvDetailDate = itemView.findViewById(R.id.tv_detail_date);
            tvDetailContents = itemView.findViewById(R.id.tv_detail_content);
            tvDetailContents.setLayerType(View.LAYER_TYPE_HARDWARE, null); // sdk 19 이상은 ChromeWebView를 사용해서 ChromeWebView로 설정
        }

        public void setItem(Community_Data community) {
            tvDetailTitle.setText(community.getTitle());
            tvDetailContents.setHtml(community.getContent());
            tvDetailContents.setInputEnabled(false);
            tvDetailUsername.setText(community.getUsername());
            tvDetailCategory.setText(community.getCategoryName());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy/MM/dd HH:mm", Locale.KOREA);
            tvDetailDate.setText(simpleDateFormat.format(community.getDate()));
        }
    }

    public class ReplyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvReplyContent, tvReplyUsername;
        private ImageView ivDeleteReply;
        public ReplyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvReplyUsername = itemView.findViewById(R.id.tv_reply_username);
            tvReplyContent = itemView.findViewById(R.id.tv_reply_content);
            ivDeleteReply = itemView.findViewById(R.id.iv_delete_reply);
        }
        public void setItem(Reply reply) {
            tvReplyUsername.setText(reply.getUsername());
            tvReplyContent.setText(reply.getContent());
            if(reply.getToken().equals(token))
            {
                ivDeleteReply.setVisibility(View.VISIBLE);
                ivDeleteReply.setOnClickListener(v->{
                    DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                    AlertDialog.Builder alert = new AlertDialog.Builder(itemView.getContext());
                    alert.setTitle("Reply Delete?");
                    alert.setPositiveButton("accept", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            db.child("Reply")
                                    .child(Q_category).
                                    child(Q_DBkey).child(reply.getReply_key()).removeValue();
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
            else
            { ivDeleteReply.setVisibility(View.INVISIBLE);}
        }

    }
}