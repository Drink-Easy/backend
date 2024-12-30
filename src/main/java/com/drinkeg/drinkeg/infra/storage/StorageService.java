package com.drinkeg.drinkeg.infra.storage;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StorageService {
    public String uploadFile(MultipartFile file, StoragePathName path);
    public List<String> uploadFiles(List<MultipartFile> files, StoragePathName path);
    public void deleteFile(String fileName);
}
