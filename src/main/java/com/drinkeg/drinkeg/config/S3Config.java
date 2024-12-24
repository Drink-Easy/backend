package com.drinkeg.drinkeg.config;


import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class S3Config {
    @Value("${cloud.aws.credentials.accessKey}")
    private String s3AccessKey;

    @Value("${cloud.aws.credentials.secretKey}")
    private String s3SecretKey;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.path.wineNews}")
    private String wineNewsPaths;

    @Value("${cloud.aws.s3.path.wineNews}")
    private String wineImagePaths;

    @Bean
    public AWSCredentials awsCredentials(){
        return new BasicAWSCredentials(s3AccessKey, s3SecretKey);
    }

    @Bean
    public AWSStaticCredentialsProvider awsStaticCredentialsProvider(AWSCredentials awsCredentials){
        return new AWSStaticCredentialsProvider(awsCredentials);
    }

    @Bean
    public AmazonS3 amazonS3(){
        AWSCredentials  awsCredentials = new BasicAWSCredentials(s3AccessKey, s3SecretKey);

        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withRegion(region)
                .build();
    }
}
