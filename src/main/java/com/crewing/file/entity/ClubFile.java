package com.crewing.file.entity;

import com.crewing.club.dto.ClubInfoResponse;
import com.crewing.club.entity.Club;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ClubFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileId;

    @ManyToOne(fetch = FetchType.LAZY) // File(N) : Club(1)
    @JoinColumn(name="club_id")
    private Club club;

    @Column(nullable = false)
    private String imageUrl;

    @Getter
    @Builder
    public static class ImageInfo {
        private Long fileId;
        private String imageUrl;
    }

    public ImageInfo toDto(){
        return ImageInfo.builder()
                .fileId(this.fileId)
                .imageUrl(this.imageUrl)
                .build();
    }
}
