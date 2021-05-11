package com.jjaln.dailychart.adapter;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.jjaln.dailychart.R;

public class Exchange_List_ViewHolder extends RecyclerView.ViewHolder
{
    public ImageView icon;
    public TextView name;

    public Exchange_List_ViewHolder(View itemView)
    {
        super(itemView);
        icon = (ImageView)itemView.findViewById(R.id.iv_exchange);
    }
}
