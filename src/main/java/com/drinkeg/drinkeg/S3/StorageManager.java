package com.drinkeg.drinkeg.S3;

import org.springframework.web.multipart.MultipartFile;

public interface StorageManager {
    public String uploadFile(MultipartFile file, String keyPath);
    public void deleteFile(String keyPath);
}
