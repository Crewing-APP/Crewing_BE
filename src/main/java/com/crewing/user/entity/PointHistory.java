package com.crewing.user.entity;

import com.crewing.common.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PointHistory extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private int point;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private PointHistoryType type;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Builder
    public PointHistory(int point, PointHistoryType type, User user) {
        this.point = point;
        this.type = type;
        this.user = user;
    }
}
