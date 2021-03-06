package com.jjaln.dailychart.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jjaln.dailychart.R;
import com.jjaln.dailychart.feature.News;
import com.jjaln.dailychart.feature.NewsCrap;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.News_List_ViewHolder> {
    private List<News> news;
    private Context context;


    public NewsListAdapter(List<News> news, Context context)
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
        private LinearLayout linearLayout;
        private ImageView ivScrap;
        private SharedPreferences pref;
        private String token;
        public News_List_ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNewsTitle = itemView.findViewById(R.id.tv_newsTitle);
            tvNewsDesc = itemView.findViewById(R.id.tv_newsDesc);
            linearLayout = itemView.findViewById(R.id.item_contents);
            ivScrap = itemView.findViewById(R.id.iv_scrap);
            //??????
            pref = context.getSharedPreferences("pref", MODE_PRIVATE);
            token = pref.getString("token", "");

            linearLayout.setOnClickListener(v->{
                int pos = getAdapterPosition();
                Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(news.get(pos).getUrl()));
                context.startActivity(mIntent);
            });
        }

        public void setNewsItem(News news){
            tvNewsTitle.setText(news.getTitle());
            tvNewsDesc.setText(news.getDesc());
            ivScrap.setOnClickListener(v->{
                DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                AlertDialog.Builder alert = new AlertDialog.Builder(itemView.getContext());
                alert.setTitle("Wanna Scrap?");
                alert.setPositiveButton("accept", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NewsCrap mScrap = new NewsCrap();
                        String temp = db.child("Scrap").child(token).push().getKey();
                        mScrap.setNews(news);
                        mScrap.setDb_key(temp);
                        db.child("Scrap").child(token).child(mScrap.getDb_key()).setValue(mScrap);
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
}
