package com.crewing.file.entity;

import com.crewing.club.entity.Club;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileId;

    @ManyToOne // File(N) : Club(1)
    @JoinColumn(name="club_id")
    private Club club;

    @Column(nullable = false)
    private String imageUrl;
}
