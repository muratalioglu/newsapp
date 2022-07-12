package com.example.newsapp.service;

import com.example.newsapp.dto.NewsDTO;
import com.example.newsapp.dto.NewsInDTO;
import com.example.newsapp.model.News;
import com.example.newsapp.repository.NewsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
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
                                news.getId(),
                                news.getTitle(),
                                news.getContent(),
                                news.getImageUrl(),
                                news.getCreateTime(),
                                news.getUpdateTime()
                        )
                )
                .collect(Collectors.toList());
    }

    @Override
    public Integer createNews(NewsInDTO dto) {

        String title = dto.getTitle().trim();
        String content = dto.getContent().trim();

        validateTitleExistence(title);

        News news = new News();
        news.setTitle(title);
        news.setContent(content);
        news.setImageUrl(dto.getImageUrl());

        newsRepository.save(news);

        return news.getId();
    }

    @Override
    public void updateNews(Integer id, String title, String content, String imageUrl) {

        News news = getNews(id);
        if (news == null)
            throw new RuntimeException(String.format("News with id %s not found.", id));

        boolean updated = false;

        if (title != null) {
            title = title.trim();

            if (!title.equals(news.getTitle())) {
                news.setTitle(title);
                updated = true;
            }
        }

        if (content != null) {
            content = content.trim();

            if (!content.equals(news.getContent())) {
                news.setContent(content);
                updated = true;
            }
        }

        if (imageUrl == null && news.getImageUrl() != null) {
            news.setImageUrl(null);
            updated = true;
        } else if (imageUrl != null && !imageUrl.equals(news.getImageUrl())) {
            news.setImageUrl(imageUrl);
            updated = true;
        }

        if (updated)
            saveNews(news);
    }

    @Override
    public void deleteNews(Integer id) {

        News news = getNews(id);
        if (news == null)
            throw new RuntimeException(String.format("News with id %s not found.", id));

        news.setDeleted(true);
        news.setDeleteTime(new Timestamp(System.currentTimeMillis()));

        saveNews(news);
    }

    private void saveNews(News news) {
        newsRepository.save(news);
    }

    private News getNews(Integer id) {
        return newsRepository.findByIdAndDeletedFalse(id);
    }

    private void validateTitleExistence(String title) {

        boolean titleExists = newsRepository.existsByTitleIgnoreCaseAndDeletedFalse(title);

        if (titleExists)
            throw new RuntimeException("The title in use!");
    }
}