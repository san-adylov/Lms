package com.app.lms.service.serviceImpl;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.InvalidMimeTypeException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class S3FileService {


    @Value("${aws_bucket_name}")
    private String bucketName;

    private final AmazonS3 s3Client;



    public String uploadFile(MultipartFile file) {
        File fileObj = convertMultiPartFileToFile(file);
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        s3Client.putObject(new PutObjectRequest(bucketName, fileName, fileObj));
        String url = s3Client.getUrl(bucketName, fileName).toString();
        return url;
    }


    public boolean deleteFile(String fileName) {
        try {
            s3Client.deleteObject("peaksoftlms-b9", fileName);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public byte[] downloadFile(String fileName) {
        S3Object s3Object = s3Client.getObject(bucketName, fileName);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        try {
            return IOUtils.toByteArray(inputStream);
        } catch (InvalidMimeTypeException | IOException e) {
            log.error("При загрузке файла произошла ошибка: {}", fileName, e);
        }
        return new byte[0];
    }

    private File convertMultiPartFileToFile(MultipartFile file) {
        File convertedFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            log.error("Ошибка преобразования multipartFile в файл", e);
        }
        return convertedFile;
    }
}