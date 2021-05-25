package com.jjaln.dailychart.feature;

import java.util.Date;

import lombok.Data;

@Data
public class Question {
    private String title;
    private String content;
    private String Token;
    private String categoryName;
    private int categoryId;
    private String username;
    private String DBKey;
    private Date date;
}
