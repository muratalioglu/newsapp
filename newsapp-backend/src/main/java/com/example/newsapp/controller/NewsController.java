package com.example.newsapp.controller;

import com.example.newsapp.dto.NewsDTO;
import com.example.newsapp.dto.NewsInDTO;
import com.example.newsapp.service.NewsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
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

    @PatchMapping("{id}")
    public ResponseEntity<?> updateNews(@PathVariable @Positive Integer id,
                                        @RequestParam(required = false) String title,
                                        @RequestParam(required = false) String content,
                                        @RequestParam(required = false) String imageUrl) {

        newsService.updateNews(id, title, content, imageUrl);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteNews(@PathVariable @Positive Integer id) {

        newsService.deleteNews(id);

        return ResponseEntity.noContent().build();
    }
}