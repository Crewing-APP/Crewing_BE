package com.crewing.club.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Status {
    ACCEPT("ACCEPT"),
    HOLD("HOLD"),
    RETURN("RETURN"),
    UNDEFINED("UNDEFINED");

    private final String key;
}
