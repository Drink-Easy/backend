package com.drinkeg.drinkeg.infra.storage;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public enum StoragePathName {
    WINE("wine"),
    WINE_CLASS("wine-class"),
    MEMBER_PROFILE("member/profile"),
    BANNER("banner");

    private final String path;
}
