package com.crewing.file.service;

import com.crewing.club.entity.Club;
import com.crewing.file.entity.ClubFile;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

public interface FileService {
    List<String> uploadMultiFile(List<MultipartFile> multipartFileList) throws IOException;
    String uploadFile(MultipartFile file) throws IOException;
    void deleteMultiFile(List<String> fileUrlList);
    void deleteFile(String fileUrl);
    List<ClubFile> createClubFile(Club club, List<String> fileUrl);
}
