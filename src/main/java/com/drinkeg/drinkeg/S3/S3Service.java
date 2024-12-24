package com.drinkeg.drinkeg.S3;

import com.drinkeg.drinkeg.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.domain.Uuid;
import com.drinkeg.drinkeg.exception.GeneralException;
import com.drinkeg.drinkeg.repository.UuidRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class S3Service implements StorageService {
    private final UuidRepository uuidRepository;
    private final S3Manager s3Manager;

    private String generateKeyPath(Uuid uuid, String path) {
        return path + '/' + uuid.getUuid();
    }

    public String uploadFile(MultipartFile file, StoragePathName storagePathName) {
        try {
            Uuid uuid = uuidRepository.save(Uuid.builder()
                    .uuid(UUID.randomUUID().toString())
                    .build());

            String url = s3Manager.uploadFile(file, generateKeyPath(uuid, storagePathName.getPath()));

            return url;
        } catch (DataIntegrityViolationException e) { // 유니크 제약조건 위반시 발생
            throw new GeneralException(ErrorStatus.FILE_UPLOAD_FAILED);
        }
    }

    @Transactional
    public List<String> uploadFiles(List<MultipartFile> files, StoragePathName storagePathName) {
        List<String> FileUrls = new ArrayList<>();
        for (MultipartFile file : files) {
            try {
                Uuid uuid = uuidRepository.save(Uuid.builder()
                        .uuid(UUID.randomUUID().toString())
                        .build());

                String url = s3Manager.uploadFile(file, generateKeyPath(uuid, storagePathName.getPath()));

                FileUrls.add(url);
            } catch (DataIntegrityViolationException e) { // 유니크 제약조건 위반시 발생
                throw new GeneralException(ErrorStatus.FILE_UPLOAD_FAILED);
            }
        }
        return FileUrls;
    }

    public void deleteFile(String url) {
        String keyPath = URI.create(url)
                .getPath()
                .replaceFirst("^/", "");

        String uuid = keyPath.substring(keyPath.lastIndexOf('/') + 1);

        try {
            uuidRepository.deleteByUuid(uuid);
        } catch (RuntimeException e) {
            throw new GeneralException(ErrorStatus.FILE_DELETE_FAILED);
        }

        s3Manager.deleteFile(keyPath);
    }
}