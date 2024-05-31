package com.crewing.club.service;

import com.crewing.club.dto.*;
import com.crewing.club.entity.Club;
import com.crewing.club.entity.Status;
import com.crewing.user.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ClubService {
    ClubCreateResponse createClub(ClubCreateRequest clubCreateRequest, User user, MultipartFile profile, List<MultipartFile> images) throws IOException;
    ClubCreateResponse updateClub(Long clubId, ClubUpdateRequest clubUpdateRequest, User user, MultipartFile profile, List<MultipartFile> images, List<String> deletedImages) throws IOException;
    void deleteClub(Long clubId, User user);
    ClubCreateResponse changeStatus(ClubChangeStatusRequest clubChangeStatusRequest, User user);
}
