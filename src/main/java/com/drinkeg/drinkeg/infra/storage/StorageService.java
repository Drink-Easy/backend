package com.drinkeg.drinkeg.infra.storage;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StorageService {
    String uploadFile(MultipartFile file, StoragePathName path);
    List<String> uploadFiles(List<MultipartFile> files, StoragePathName path);
    void deleteFile(String fileName);
}
