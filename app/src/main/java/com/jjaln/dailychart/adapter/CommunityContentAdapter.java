package com.jjaln.dailychart.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.jjaln.dailychart.R;
import com.jjaln.dailychart.contents.community.CommunityContentsActivity;
import com.jjaln.dailychart.feature.Community_Data;
import com.jjaln.dailychart.feature.Contents;
import com.jjaln.dailychart.feature.Reply;

import java.util.List;


public class CommunityContentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Contents> items;
    private CommunityContentsActivity communityDetailActivity;
    private String token;
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

        private AppCompatButton btnReply;
        private TextView tvDetailTitle, tvDetailUsername, tvDetailCategory, tvDetailContents;

        public DetailContentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDetailTitle = itemView.findViewById(R.id.tv_detail_title);
            tvDetailUsername = itemView.findViewById(R.id.tv_detail_username);
            tvDetailCategory = itemView.findViewById(R.id.tv_detail_category);
            tvDetailContents = itemView.findViewById(R.id.tv_detail_content);
            btnReply = itemView.findViewById(R.id.btn_reply);
            btnReply.setOnClickListener(v -> {
                communityDetailActivity.showReplyInput();
            });
        }

        public void setItem(Community_Data community) {
            tvDetailTitle.setText(community.getTitle());
            tvDetailContents.setText(community.getContent());
            tvDetailUsername.setText(community.getUsername());
            tvDetailCategory.setText(community.getCategoryName());
        }
    }

    public class ReplyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvReplyContent, tvReplyUsername;

        public ReplyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvReplyUsername = itemView.findViewById(R.id.tv_reply_username);
            tvReplyContent = itemView.findViewById(R.id.tv_reply_content);
        }
        public void setItem(Reply reply) {
            tvReplyUsername.setText(reply.getUsername());
            tvReplyContent.setText(reply.getContent());
        }
    }
}