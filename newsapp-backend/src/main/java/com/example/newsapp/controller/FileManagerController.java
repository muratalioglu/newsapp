package com.example.newsapp.controller;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import javax.imageio.ImageIO;
import javax.validation.constraints.NotNull;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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

    @Value("${AWS_BUCKET_NAME}")
    private String bucketName;

    @PostMapping
    @CrossOrigin(origins = "http://localhost:3000", exposedHeaders = { HttpHeaders.LOCATION })
    public ResponseEntity<?> uploadFile(@RequestParam(name = "file") @NotNull MultipartFile multipartFile) {

        byte[] resizedImageByteArray = resizeImage(multipartFile);

        AWSCredentials credentials = new BasicAWSCredentials(awsAccessKey, awsSecretKey);

        AmazonS3 s3Client =
                AmazonS3ClientBuilder
                        .standard()
                        .withCredentials(new AWSStaticCredentialsProvider(credentials))
                        .withRegion(Regions.EU_CENTRAL_1)
                        .build();

        ObjectMetadata data = new ObjectMetadata();
        data.setContentType(multipartFile.getContentType());
        data.setContentLength(resizedImageByteArray.length);

        s3Client.putObject(
                bucketName,
                multipartFile.getOriginalFilename(),
                new ByteArrayInputStream(resizedImageByteArray),
                data
        );

        String url = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host(bucketName + ".s3.amazonaws.com")
                .path("/{filename}")
                .buildAndExpand(multipartFile.getOriginalFilename())
                .toUriString();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header(HttpHeaders.LOCATION, url)
                .build();
    }

    private byte[] resizeImage(MultipartFile sourceFile) {

        BufferedImage originalImage = null;
        try {
            originalImage = ImageIO.read(new ByteArrayInputStream(sourceFile.getBytes()));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

        if (originalImage == null)
            throw new RuntimeException("Image cannot be null!");

        BufferedImage resultImage = new BufferedImage(700, 400, BufferedImage.TYPE_INT_RGB);
        resultImage.getGraphics().drawImage(originalImage, 0, 0, 700, 400, null);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(resultImage, "jpg", byteArrayOutputStream);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return byteArrayOutputStream.toByteArray();
    }
}