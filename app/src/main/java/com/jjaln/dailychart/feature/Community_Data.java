package com.jjaln.dailychart.feature;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Community_Data implements Serializable {
    private BigInteger id;
    private String title;
    private String categoryName;
    private String token;
    private String username;
    private String content;
    private String DBKEy;
    private Date date;

    private Reply reply;
}
