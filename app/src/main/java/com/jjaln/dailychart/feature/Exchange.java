package com.jjaln.dailychart.feature;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Exchange {
    private int img;
    private String uri;
    private String name;
}
