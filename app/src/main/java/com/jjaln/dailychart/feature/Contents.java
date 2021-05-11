package com.jjaln.dailychart.feature;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Contents {
    private int type;
    private Object object;

    public Contents(int type) {
        this.type=type;
    }
}
