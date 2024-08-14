package com.crewing.applicant.entity;

import com.crewing.club.entity.Club;
import com.crewing.common.entity.BaseTimeEntity;
import com.crewing.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Applicant extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "applicant_id")
    private Long applicantId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    private Club club;

    @Column(nullable = false)
    @Convert(converter = ApplicantStatusConverter.class)
    private Status status;
}
