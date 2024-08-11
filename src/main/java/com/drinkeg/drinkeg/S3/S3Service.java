package com.drinkeg.drinkeg.S3;


import com.drinkeg.drinkeg.domain.Uuid;
import com.drinkeg.drinkeg.repository.UuidRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class S3Service {


    private final AmazonS3Manager s3Manager;
    private final UuidRepository uuidRepository;

    public String SaveImage(MultipartFile image){

        String uuid = UUID.randomUUID().toString();
        Uuid savedUuid = uuidRepository.save(Uuid.builder()
                .uuid(uuid).build());

        String pictureUrl = s3Manager.uploadFile(s3Manager.generateWineNewsKeyName(savedUuid), image);

        return pictureUrl;
    }
}
