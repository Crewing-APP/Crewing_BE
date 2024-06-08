package com.crewing.applicant.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Status {
    WAIT("WAIT"),
    DOC("DOC"),
    INTERVIEW("INTERVIEW");

    private final String key;
}
