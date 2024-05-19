package com.crewing.user.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    GUEST("GUEST"),
    USER("USER"),
    STUDENT("ADMIN");

    private final String key;
}
