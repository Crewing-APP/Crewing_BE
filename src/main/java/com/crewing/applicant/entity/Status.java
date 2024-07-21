package com.crewing.applicant.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Status {
    WAIT("WAIT"),
    DOC("DOC"),
    INTERVIEW("INTERVIEW"),
    DOC_FAIL("DOC_FAIL"),
    FINAL_FAIL("FINAL_FAIL");

    private final String key;
}
