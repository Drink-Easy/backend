package com.drinkeg.drinkeg.S3;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.drinkeg.drinkeg.config.S3Config;
import com.drinkeg.drinkeg.domain.Uuid;
import com.drinkeg.drinkeg.repository.UuidRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class S3Service {


    private final AmazonS3Manager s3Manager;
    private final UuidRepository uuidRepository;

    public String SaveImage(MultipartFile image){ //이미지 한개 저장할 때


        String uuid = UUID.randomUUID().toString();
        Uuid savedUuid = uuidRepository.save(Uuid.builder()
                .uuid(uuid).build());

        String pictureUrl = s3Manager.uploadFile(s3Manager.generateWineNewsKeyName(savedUuid), image);

        System.out.println("pictureUrl: " + pictureUrl);

        return pictureUrl;
    }

    public void deleteFile(String pictureUrl){ // 사진 삭제

        String savedUuid = pictureUrl.substring(pictureUrl.lastIndexOf("/wineNews/") + "/wineNews/".length());
        Uuid uuid = uuidRepository.findByUuid(savedUuid);

        s3Manager.deleteFile(s3Manager.generateWineNewsKeyName(uuid));

    }

    public void SaveImages(List<MultipartFile> pictureList){ // List로 들어온 multipartFile 이미지 저장

        for(MultipartFile picture : pictureList){   // picture마다 유일한 URL 값 생성
            String uuid = UUID.randomUUID().toString();
            Uuid savedUuid = uuidRepository.save(Uuid.builder().uuid(uuid).build());

            String pictureUrl = s3Manager.uploadFile(s3Manager.generateWineNewsKeyName(savedUuid), picture);

        }
    }
}
