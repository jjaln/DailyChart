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

    private int coin_img;
    private String coin_name;
    private String market_price;
    private String flucate_rate;
    private String flucate_price;
}
