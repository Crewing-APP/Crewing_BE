package com.crewing.user.entity;

import com.crewing.common.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Getter
@NoArgsConstructor
@DynamicUpdate
@Setter
@Table(name = "users")
public class User extends BaseTimeEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "user_id")
    private Long id;

    @Column
    private String email;

    @Column
    private String password;

    @Column
    private String nickname;

    @Column
    private String imageUrl;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @Column
    private String socialId;
    @Column
    private String refreshToken;

    //추가 회원 가입
    @Column
    private String birth;

    @Column
    private String gender;
    @Column
    private String name;

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<Interest> interests = new ArrayList<>();

    @Builder
    public User(String email, String password, String nickname, String imageUrl, Role role, SocialType socialType,
                String socialId, String refreshToken) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.imageUrl = imageUrl;
        this.role = role;
        this.socialType = socialType;
        this.socialId = socialId;
        this.refreshToken = refreshToken;
    }

    public void authorizeUser() {
        this.role = Role.USER;
    }

    // 비밀번호 암호화 메소드
    public void passwordEncode(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }

    public void updateRefreshToken(String updateRefreshToken) {
        this.refreshToken = updateRefreshToken;
    }

    public void addInterest(Interest interest) {
        interest.setUser(this);
        this.interests.add(interest);
    }

    public void updateInterests(List<Interest> interests) {
        this.interests.clear();

        interests.forEach(
                this::addInterest
        );
    }

    public void signUpOauth(String birth, String gender, String name, List<Interest> interests) {
        this.birth = birth;
        this.gender = gender;
        this.name = name;

        updateInterests(interests);
        authorizeUser();
    }


}
