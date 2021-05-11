package com.jjaln.dailychart.feature;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Coin {
//    public String key;
//    public String acc_trade_value;
//    public String acc_trade_value_24H;
    public String closing_price;
    public String fluctate_24H;
    public String fluctate_rate_24H;
//    public String max_price;
//    public String min_price;
//    public String opening_price;
//    public String prev_closing_price;
//    public String units_traded;
//    public String units_traded_24H;
}
