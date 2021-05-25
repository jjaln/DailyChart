package com.jjaln.dailychart.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.jjaln.dailychart.MainActivity;
import com.jjaln.dailychart.R;
import com.jjaln.dailychart.contents.community.CommunityContentsActivity;
import com.jjaln.dailychart.feature.Community_Data;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class CommunityListAdapter extends RecyclerView.Adapter<CommunityListAdapter.CommunityViewHolder>{
    private List<Community_Data> communities;
    private Context mContext;
    private String token;
    private int i=0;
    private static final String TAG = "CommunityAdapter";
    public CommunityListAdapter(List<Community_Data> communities, Context mContext, String token) {
        this.communities = communities;
        this.mContext = mContext;
        this.token = token;
    }

    @NonNull
    @Override
    public CommunityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.community_item, parent, false);
        return new CommunityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommunityViewHolder holder, int position) {
        holder.setCommunityItem(communities.get(position));
    }

    @Override
    public int getItemCount() {
        return communities.size();
    }

    public class CommunityViewHolder extends RecyclerView.ViewHolder{

        private TextView tvCommunityTitle,tvCommunityCategory, tvUsername, tvCommunityDate, in;
        private AppCompatButton btnLike;

        public CommunityViewHolder(@NonNull View itemView) {
            super(itemView);
            in = itemView.findViewById(R.id.textView2);
            tvCommunityTitle = itemView.findViewById(R.id.tv_community_title);
            tvCommunityCategory = itemView.findViewById(R.id.tv_community_category);
            tvUsername = itemView.findViewById(R.id.tv_username);
            tvCommunityDate = itemView.findViewById(R.id.tv_community_date);

            itemView.setOnClickListener(v->{
                int pos = getAdapterPosition();
                Intent intent = new Intent(mContext, CommunityContentsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("community",communities.get(pos));
                intent.putExtra("pos",pos);
                mContext.startActivity(intent);
            });
        }

        public void setCommunityItem(Community_Data community){
            tvCommunityTitle.setText(community.getTitle());
            tvCommunityCategory.setText(community.getCategoryName());
            tvUsername.setText(community.getUsername());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd HH:mm", Locale.KOREA);
            tvCommunityDate.setText(simpleDateFormat.format(community.getDate()));

            tvCommunityCategory.setVisibility(View.VISIBLE);
            in.setVisibility(View.VISIBLE);
        }
    }
}