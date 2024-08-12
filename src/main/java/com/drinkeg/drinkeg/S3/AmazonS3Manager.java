package com.drinkeg.drinkeg.S3;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.drinkeg.drinkeg.config.S3Config;
import com.drinkeg.drinkeg.domain.Uuid;
import com.drinkeg.drinkeg.repository.UuidRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class AmazonS3Manager{

    private final AmazonS3 amazonS3;

    private final S3Config s3Config;


    public String uploadFile(String keyName, MultipartFile file){

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        try {
            amazonS3.putObject(new PutObjectRequest(s3Config.getBucket(), keyName, file.getInputStream(), metadata));
        }catch (IOException e){
            log.error("error at AmazonS3Manager uploadFile : {}", (Object) e.getStackTrace());
        }

        return amazonS3.getUrl(s3Config.getBucket(), keyName).toString();
    }

    // S3에 들어갈 KeyName 만들어주기 (어떤 디렉토리 안으로 들어갈지 반영)
    public String generateWineNewsKeyName(Uuid uuid) {
        return s3Config.getWineNewsPaths() + '/' + uuid.getUuid();
    }


    public void deleteFile(String fileName){
        try {
            amazonS3.deleteObject(new DeleteObjectRequest(s3Config.getBucket(), fileName));
        }catch (AmazonServiceException e) {
            // AWS 서비스에서 발생한 오류 처리
            log.error("Amazon S3 service error: {}", e.getErrorMessage(), e);
        } catch (SdkClientException e) {
            // 클라이언트 측에서 발생한 오류 처리
            log.error("Amazon S3 client error: {}", e.getMessage(), e);
        }
    }

}