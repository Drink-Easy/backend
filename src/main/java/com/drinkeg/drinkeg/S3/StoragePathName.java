package com.drinkeg.drinkeg.S3;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public enum StoragePathName {
    WINE("wine"),
    WINE_CLASS("wine_class"),
    MEMBER_PROFILE("member/profile");

    private final String path;
}
