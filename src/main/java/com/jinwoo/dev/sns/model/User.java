package com.jinwoo.dev.sns.model;


import com.jinwoo.dev.sns.model.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class User implements UserDetails {

    private Integer id;
    private String userName;
    private String password;
    private UserRole userRole;
    private Timestamp registeredAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public static User fromEntity(UserEntity entity){
        return new User(
                entity.getId()
                , entity.getUserName()
                , entity.getPassword()
                , entity.getRole()
                , entity.getRegisteredAt()
                , entity.getUpdatedAt()
                , entity.getDeletedAt()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.getUserRole().toString()));
    }

    @Override
    public String getUsername() {
        return this.userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
