package com.jjaln.dailychart.feature;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Reply implements Serializable {
    private String content;
    private String username;
    private String Token;
    private String reply_key;
    private Contents community;
}
