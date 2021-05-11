package com.jjaln.dailychart.Recycler.exchange;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jjaln.dailychart.R;

import java.util.ArrayList;

public class Exchange_List_RecyclerAdapter extends RecyclerView.Adapter<Exchange_List_ViewHolder>
{
    private ArrayList<Exchange_List_Data> Exchange_List;

    public Exchange_List_RecyclerAdapter(ArrayList<Exchange_List_Data> list)
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
        final Exchange_List_Data data = Exchange_List.get(position);

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
