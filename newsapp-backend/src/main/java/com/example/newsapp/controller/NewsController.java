package com.example.newsapp.controller;

import com.example.newsapp.dto.NewsDTO;
import com.example.newsapp.service.NewsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/news")
@Slf4j
public class NewsController {

    private final NewsService newsService;

    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping
    public ResponseEntity<?> getNews() {

        List<NewsDTO> newsDTOList = newsService.getNews();

        return newsDTOList.isEmpty() ?
                ResponseEntity.noContent().build() :
                ResponseEntity.ok(newsDTOList);
    }
}