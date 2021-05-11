package com.jjaln.dailychart.Recycler.News;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jjaln.dailychart.R;

import java.util.List;

public class News_List_RecyclerAdapter extends RecyclerView.Adapter<News_List_RecyclerAdapter.News_List_ViewHolder> {
    private List<News_List_Data> news;
    private Context context;

    public News_List_RecyclerAdapter(List<News_List_Data> news, Context context)
    {
        this.news = news;
        this.context= context;
    }

    @NonNull
    @Override
    public News_List_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);
        return new News_List_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull News_List_ViewHolder holder, int position) {
        holder.setNewsItem(news.get(position));
    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    public class News_List_ViewHolder extends RecyclerView.ViewHolder{

        private TextView tvNewsTitle,tvNewsDesc;

        public News_List_ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNewsTitle = itemView.findViewById(R.id.tv_newsTitle);
            tvNewsDesc = itemView.findViewById(R.id.tv_newsDesc);

            //상세
            itemView.setOnClickListener(v->{
                int pos = getAdapterPosition();
                Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(news.get(pos).getUrl()));
                context.startActivity(mIntent);
            });
        }

        public void setNewsItem(News_List_Data news){
            tvNewsTitle.setText(news.getTitle());
            tvNewsDesc.setText(news.getDesc());
        }
    }
}
