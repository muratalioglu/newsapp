package com.example.newsapp.controller;

import com.example.newsapp.service.FileManagerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/files")
@Slf4j
public class FileManagerController {

    private final FileManagerService fileManagerService;

    public FileManagerController(FileManagerService fileManagerService) {
        this.fileManagerService = fileManagerService;
    }

    @PostMapping
    @CrossOrigin(origins = "http://localhost:3000", exposedHeaders = { HttpHeaders.LOCATION })
    public ResponseEntity<?> uploadFile(@RequestParam(name = "file") MultipartFile multipartFile) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header(HttpHeaders.LOCATION, fileManagerService.uploadFile(multipartFile))
                .build();
    }
}