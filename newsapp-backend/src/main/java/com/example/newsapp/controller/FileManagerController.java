package com.example.newsapp.controller;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/files")
@Slf4j
public class FileManagerController {

    @Autowired
    private Environment env;

    @Value("${AWS_ACCESS_KEY}")
    private String awsAccessKey;

    @Value("${AWS_SECRET_KEY}")
    private String awsSecretKey;

    @PostMapping
    public ResponseEntity<?> uploadFile(@RequestParam(name = "file") MultipartFile multipartFile) {

        AWSCredentials credentials = new BasicAWSCredentials(awsAccessKey, awsSecretKey);

        AmazonS3 s3Client =
                AmazonS3ClientBuilder
                        .standard()
                        .withCredentials(new AWSStaticCredentialsProvider(credentials))
                        .withRegion(Regions.EU_CENTRAL_1)
                        .build();

        final String bucketName = "newsapp-bucket-807863322";

        ObjectMetadata data = new ObjectMetadata();
        data.setContentType(multipartFile.getContentType());
        data.setContentLength(multipartFile.getSize());

        PutObjectResult result = null;
        try {
            result = s3Client.putObject(
                    bucketName,
                    multipartFile.getOriginalFilename(),
                    multipartFile.getInputStream(),
                    data
            );
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

        String imageUrl = "https://" + bucketName + ".s3.amazonaws.com/" + multipartFile.getOriginalFilename();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("url", imageUrl)
                .build();
    }
}