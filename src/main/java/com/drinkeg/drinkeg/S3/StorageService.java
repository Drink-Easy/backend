package com.drinkeg.drinkeg.S3;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StorageService {
    public String uploadFile(MultipartFile file, String path);
    public List<String> uploadFiles(List<MultipartFile> files, String path);
    public void deleteFile(String fileName);
}
