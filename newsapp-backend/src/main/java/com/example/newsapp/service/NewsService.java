package com.example.newsapp.service;

import com.example.newsapp.dto.NewsDTO;

import java.util.List;

public interface NewsService {

    List<NewsDTO> getNews();
}