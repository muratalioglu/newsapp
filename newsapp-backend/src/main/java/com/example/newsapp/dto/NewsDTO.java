package com.example.newsapp.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class NewsDTO {

    String title;

    String content;

    String imageUrl;

    Timestamp createTime;

    Timestamp updateTime;
}