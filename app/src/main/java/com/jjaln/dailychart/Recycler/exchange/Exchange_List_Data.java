package com.jjaln.dailychart.Recycler.exchange;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Exchange_List_Data {
    private int img;
    private String uri;
    private String name;
}
