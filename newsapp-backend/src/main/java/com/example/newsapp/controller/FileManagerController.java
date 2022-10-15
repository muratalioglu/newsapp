package com.example.newsapp.controller;

import com.example.newsapp.service.FileManagerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/files")
@Slf4j
public class FileManagerController {

    private final FileManagerService fileManagerService;

    public FileManagerController(FileManagerService fileManagerService) {
        this.fileManagerService = fileManagerService;
    }

    @PostMapping
    @CrossOrigin(exposedHeaders = {HttpHeaders.LOCATION})
    public ResponseEntity<?> uploadFile(@RequestParam(name = "file") MultipartFile multipartFile) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header(HttpHeaders.LOCATION, fileManagerService.uploadFile(multipartFile))
                .build();
    }
}