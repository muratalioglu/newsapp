package com.example.newsapp.repository;

import com.example.newsapp.model.News;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NewsRepository extends JpaRepository<News, Integer> {

    List<News> findAllByDeletedFalse();

    boolean existsByTitleIgnoreCaseAndDeletedFalse(String title);
}