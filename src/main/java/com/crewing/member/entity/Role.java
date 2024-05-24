package com.crewing.member.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    MEMBER("MEMBER"),
    MANAGER("MANAGER");

    private final String key;
}
