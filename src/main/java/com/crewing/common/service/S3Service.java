package com.crewing.common.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j //todo : 버킷 생성 및 이미지 업로드 , 수정 , 삭제 로직 추가
public class S3Service {


    public String uploadImage(MultipartFile multipartFile) {

        return "example";
    }
}
