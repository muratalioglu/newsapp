package com.example.newsapp.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileManagerService {

    String uploadFile(MultipartFile multipartFile);
}