package com.laioffer.staybooking.service;

import com.google.cloud.storage.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.beans.factory.annotation.Value;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.laioffer.staybooking.exception.GCSUploadException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

@Service
public class ImageStorageService {
    //告诉java  bucketName 被map 到gcs.bucket ， 在Properties 已经定义好了
    @Value("${gcs.bucket}")
    private String bucketName;

    // spring frame 里面文件类型就是MultipartFile
    public String save(MultipartFile file) throws GCSUploadException {
        // 获取credentials
        Credentials credentials = null;
        try {
            // 直接从resource 路径 读取 key file
            credentials = GoogleCredentials.fromStream(getClass()
                    .getClassLoader()
                    .getResourceAsStream("credentials.json"));
            // 为什么是IOexception ？？
        } catch (IOException e) {
            throw new GCSUploadException("Failed to load GCP credentials");
        }

        Storage storage = StorageOptions.newBuilder()
                .setCredentials(credentials)
                .build()
                .getService();
        // UUID random generate name
        String filename = UUID.randomUUID().toString();
        BlobInfo blobInfo = null;
//        BlobId blobId = BlobId.of(bucketName,filename);  sean的写法
        try {
            blobInfo = storage.createFrom(
                    BlobInfo
                            .newBuilder(bucketName, filename)
                            .setContentType("image/jpeg")
                            //ACL 是什么 -》  向前端程序打开 权限，让他们可以根据url 去下载内容。
                            .setAcl(new ArrayList<>(Arrays.asList(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER))))
                            .build(),
                    file.getInputStream());
        } catch (IOException e) {
            throw new GCSUploadException("Failed to upload images to GCS");
        }

        return blobInfo.getMediaLink();
    }

}
