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

public class NewScrapAdapter extends RecyclerView.Adapter<NewScrapAdapter.NewScrap_ViewHolder> {
    private List<NewsCrap> scrap;
    private Context context;


    public NewScrapAdapter(List<NewsCrap> scrap, Context context)
    {
        this.scrap = scrap;
        this.context= context;
    }

    @NonNull
    @Override
    public NewScrap_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news_scrap, parent, false);
        return new NewScrap_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewScrap_ViewHolder holder, int position) {
        holder.setNewsItem(scrap.get(position));
    }

    @Override
    public int getItemCount() {
        return scrap.size();
    }

    public class NewScrap_ViewHolder extends RecyclerView.ViewHolder{

        private TextView tvNewsTitle,tvNewsDesc;
        private LinearLayout linearLayout;
        private ImageView ivDelete;
        private SharedPreferences pref;
        private String token;
        public NewScrap_ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNewsTitle = itemView.findViewById(R.id.tv_newsTitle);
            tvNewsDesc = itemView.findViewById(R.id.tv_newsDesc);
            linearLayout = itemView.findViewById(R.id.item_contents);
            ivDelete = itemView.findViewById(R.id.iv_scrap);
            //상세
            pref = context.getSharedPreferences("pref", MODE_PRIVATE);
            token = pref.getString("token", "");

            linearLayout.setOnClickListener(v->{
                int pos = getAdapterPosition();
                Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(scrap.get(pos).getNews().getUrl()));
                context.startActivity(mIntent);
            });
        }

        public void setNewsItem(NewsCrap nc){
            tvNewsTitle.setText(nc.getNews().getTitle());
            tvNewsDesc.setText(nc.getNews().getDesc());
            ivDelete.setOnClickListener(v->{
                DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                AlertDialog.Builder alert = new AlertDialog.Builder(itemView.getContext());
                alert.setTitle("Delete Scrap?");
                alert.setPositiveButton("accept", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       db.child("Scrap").child(token).child(nc.getDb_key()).removeValue();
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
