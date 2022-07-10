package com.example.newsapp.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    Integer id;

    @Column
    String title;

    @Column
    String content;

    @Column
    String imageUrl;

    @Column
    Timestamp createTime = new Timestamp(System.currentTimeMillis());

    @Column
    Timestamp updateTime;

    @Column
    Timestamp deleteTime;

    @Column
    Boolean deleted = false;
}