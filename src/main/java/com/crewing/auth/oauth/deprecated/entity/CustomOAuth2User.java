package com.crewing.auth.oauth.deprecated.entity;

import com.crewing.user.entity.Role;
import com.crewing.user.entity.User;
import java.util.Collection;
import java.util.Map;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

@Getter
public class CustomOAuth2User extends DefaultOAuth2User {
    private String email;
    private Role role;
    private User user;

    public CustomOAuth2User(Collection<? extends GrantedAuthority> authorities,
                            Map<String, Object> attributes, String nameAttributeKey,
                            String email, Role role) {
        super(authorities, attributes, nameAttributeKey);
        this.email = email;
        this.role = role;
    }

    public CustomOAuth2User(Collection<? extends GrantedAuthority> authorities,
                            Map<String, Object> attributes, String nameAttributeKey,
                            String email, Role role, User user) {
        super(authorities, attributes, nameAttributeKey);
        this.email = email;
        this.role = role;
        this.user = user;
    }


    public User toEntity() {
        return User.builder().build();
    }

}
