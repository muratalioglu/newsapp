package com.example.newsapp.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.google.common.util.concurrent.Uninterruptibles;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class FileManagerServiceImpl implements FileManagerService {

    @Autowired
    private Environment env;

    @Value("${AWS_ACCESS_KEY}")
    private String awsAccessKey;

    @Value("${AWS_SECRET_KEY}")
    private String awsSecretKey;

    @Value("${AWS_BUCKET_NAME}")
    private String bucketName;

    @Override
    public String uploadFile(MultipartFile multipartFile) {

        log.info("Uploading the image: {}", multipartFile.getOriginalFilename());

        File file;
        try {
            file = new File(System.getProperty("java.io.tmpdir") + "/" + multipartFile.getOriginalFilename());
            multipartFile.transferTo(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String fileFormat = determineImageFileFormat(file);
        if (fileFormat.equals("webp"))
            file = convertImageFileToJpeg(file);

        byte[] resizedImageByteArray = resizeImage(file);

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

        log.info("The image has been uploaded.");

        return UriComponentsBuilder.newInstance()
                .scheme("https")
                .host(bucketName + ".s3.amazonaws.com")
                .path("/{filename}")
                .buildAndExpand(multipartFile.getOriginalFilename())
                .toUriString();
    }

    private File convertImageFileToJpeg(File file) {

        log.info("Converting image file's extension to jpeg: {}", file.getName());

        int dotIndex = file.getName().lastIndexOf('.');
        String updatedName = file.getName().substring(0, dotIndex).concat(".jpg");

        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.directory(new File(System.getProperty("java.io.tmpdir")));
        processBuilder.command("magick", file.getName(), updatedName);

        try {
            CompletableFuture<Process> conversionProcess = processBuilder.start().onExit();

            while (!conversionProcess.isDone())
                Uninterruptibles.sleepUninterruptibly(100, TimeUnit.MILLISECONDS);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        log.info("The conversion has been completed. New name of the file: {}", updatedName);

        return new File(System.getProperty("java.io.tmpdir") + "/" + updatedName);
    }

    private String determineImageFileFormat(File file) {

        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.directory(new File(System.getProperty("java.io.tmpdir")));
        processBuilder.command("magick", "identify", file.getName());

        Process process;
        String output;
        try {
            process = processBuilder.start();

            InputStream inputStream = process.getInputStream();
            output = new String(inputStream.readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return output.split(" ")[1].toLowerCase();
    }

    private byte[] resizeImage(File file) {

        BufferedImage originalImage;
        try {
            originalImage = ImageIO.read(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
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