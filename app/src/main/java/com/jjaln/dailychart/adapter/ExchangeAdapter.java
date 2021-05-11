package com.jjaln.dailychart.adapter;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jjaln.dailychart.R;
import com.jjaln.dailychart.feature.Exchange;

import java.util.ArrayList;

public class ExchangeAdapter extends RecyclerView.Adapter<Exchange_List_ViewHolder>
{
    private ArrayList<Exchange> Exchange_List;

    public ExchangeAdapter(ArrayList<Exchange> list)
    {
        this.Exchange_List = list;
    }

    @NonNull
    @Override
    public Exchange_List_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_exchage,parent,false);

        Exchange_List_ViewHolder holder = new Exchange_List_ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Exchange_List_ViewHolder holder, int position) {
        final Exchange data = Exchange_List.get(position);

        holder.icon.setImageResource(data.getImg());
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                //Test onClick method
                //Toast.makeText(view.getContext(),data.getText(),Toast.LENGTH_SHORT).show();
                try {
                    Intent intent = view.getContext().getPackageManager().getLaunchIntentForPackage(data.getUri());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    view.getContext().startActivity(intent);
                }
                catch (Exception e)
                {
                    String url = "market://details?id="+data.getUri();
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    view.getContext().startActivity(i);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return Exchange_List.size();
    }
}
