package com.example.newsapp.controller;

import com.example.newsapp.dto.NewsDTO;
import com.example.newsapp.dto.NewsInDTO;
import com.example.newsapp.service.NewsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<?> getNews() {

        List<NewsDTO> newsDTOList = newsService.getNews();

        return newsDTOList.isEmpty() ?
                ResponseEntity.noContent().build() :
                ResponseEntity.ok(newsDTOList);
    }

    @PostMapping
    public ResponseEntity<?> createNews(@RequestBody NewsInDTO dto) {

        Integer newsId = newsService.createNews(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("NewsId", newsId.toString())
                .build();
    }
}