package com.drinkeg.drinkeg.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;


@Getter
@Setter
@RedisHash(value = "refreshToken", timeToLive =86400000L)
public class RefreshToken {


    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    //private Long id;
    @Id
    private String refresh;
    private String username;
    private String expiration;
}
