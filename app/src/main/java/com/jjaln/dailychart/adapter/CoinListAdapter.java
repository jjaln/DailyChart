package com.jjaln.dailychart.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jjaln.dailychart.contents.CoinInfo;
import com.jjaln.dailychart.R;
import com.jjaln.dailychart.feature.Coin;
import com.makeramen.roundedimageview.RoundedImageView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CoinListAdapter extends RecyclerView.Adapter<CoinListAdapter.Coin_List_ViewHolder> {

    DecimalFormat in = new DecimalFormat("###,###");
    DecimalFormat dot1 = new DecimalFormat("###.#");
    DecimalFormat dot2 = new DecimalFormat("###.##");
    private ArrayList<Coin> Coin_List;
    private Context mContext;
    public CoinListAdapter(ArrayList<Coin> list, Context context) {
        this.Coin_List = list;
        this.mContext = context;
    }

    @NonNull
    @Override
    public Coin_List_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_coin, parent, false);
        return new Coin_List_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Coin_List_ViewHolder holder, int position) {
        holder.setCoinListItem(Coin_List.get(position));
    }

    @Override
    public int getItemCount() {
        return Coin_List.size();
    }

    public String getFormat(String price) {
        String pm = "";
        if (price.charAt(0) == '-') {
            pm = "- ";
            price = price.substring(1);
        }

        Float temp_price = Float.valueOf(price);
        String res;
        if (temp_price >= 100)
            res = in.format(temp_price);
        else if (temp_price < 100 && temp_price >= 10)
            res = dot1.format(temp_price);
        else
            res = dot2.format(temp_price);
        return pm+res;
    }
    public class Coin_List_ViewHolder extends RecyclerView.ViewHolder{

        private TextView tvCoinName,tvCoinPrice,tvFlucPrice,tvFlucRate,tvCoinType;
        private RoundedImageView rivCoinImage;

        public Coin_List_ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCoinName = itemView.findViewById(R.id.coin_name);
            tvCoinType = itemView.findViewById(R.id.coin_type);
            tvCoinPrice = itemView.findViewById(R.id.market_price);
            tvFlucPrice = itemView.findViewById(R.id.fluctate_price);
            tvFlucRate = itemView.findViewById(R.id.fluctate_rate);
            rivCoinImage = (RoundedImageView)itemView.findViewById(R.id.coin_image);
            //상세
            itemView.setOnClickListener(v->{
                int pos = getAdapterPosition();
                Intent intent = new Intent(mContext, CoinInfo.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("coin_name", Coin_List.get(pos).getCoin_name());
                intent.putExtra("coin_img",Coin_List.get(pos).getCoin_img());
                mContext.startActivity(intent);
            });
        }

        public void setCoinListItem(Coin coins){
            tvCoinName.setText(coins.getCoin_name());
            tvCoinType.setText(coins.getCoin_type());
            tvCoinPrice.setText(getFormat(coins.getMarket_price()));
            tvFlucPrice.setText(getFormat(coins.getFlucate_price()));
            tvFlucRate.setText(getFormat(coins.getFlucate_rate()));
            rivCoinImage.setImageResource(coins.getCoin_img());

            if (coins.getFlucate_rate().charAt(0) == '-') {
                tvCoinPrice.setTextColor(Color.BLUE);
                tvFlucPrice.setTextColor(Color.BLUE);
                tvFlucRate.setTextColor(Color.BLUE);
            } else {
                if (Float.valueOf(coins.getFlucate_rate()) > 0) {
                    tvCoinPrice.setTextColor(Color.RED);
                    tvFlucPrice.setTextColor(Color.RED);
                    tvFlucRate.setTextColor(Color.RED);
                } else {
                    tvCoinPrice.setTextColor(Color.BLACK);
                    tvFlucPrice.setTextColor(Color.BLACK);
                    tvFlucRate.setTextColor(Color.BLACK);
                }
            }
        }
    }
}
