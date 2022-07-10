package com.example.newsapp.service;

import com.example.newsapp.dto.NewsDTO;
import com.example.newsapp.repository.NewsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;

    public NewsServiceImpl(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    @Override
    public List<NewsDTO> getNews() {
        return newsRepository.findAllByDeletedFalse().stream()
                .map(news ->
                        new NewsDTO(
                                news.getTitle(),
                                news.getContent(),
                                news.getImageUrl(),
                                news.getCreateTime(),
                                news.getUpdateTime()
                        )
                )
                .collect(Collectors.toList());
    }
}