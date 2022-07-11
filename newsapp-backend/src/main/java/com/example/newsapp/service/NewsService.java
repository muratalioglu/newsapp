package com.example.newsapp.service;

import com.example.newsapp.dto.NewsDTO;
import com.example.newsapp.dto.NewsInDTO;

import java.util.List;

public interface NewsService {

    List<NewsDTO> getNews();

    Integer createNews(NewsInDTO dto);

    void updateNews(Integer id, String title, String content, String imageUrl);
}