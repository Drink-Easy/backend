package com.drinkeg.drinkeg.infra.storage;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.drinkeg.drinkeg.global.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.global.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class S3Manager {
    private final AmazonS3 amazonS3;
    private final S3Config s3Config;

    public String uploadFile(MultipartFile file, String keyPath) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());

        try {
            amazonS3.putObject(
                    new PutObjectRequest(s3Config.getBucket(),
                            keyPath,
                            file.getInputStream(),
                            metadata)
            );
        } catch (IOException e) {
            // 로깅 필요
            throw new GeneralException(ErrorStatus.FILE_UPLOAD_FAILED);
        }

        return amazonS3.getUrl(s3Config.getBucket(), keyPath)
                .toString();
    }

    public void deleteFile(String keyPath) {
        amazonS3.deleteObject(new DeleteObjectRequest(s3Config.getBucket(), keyPath));
    }
}
