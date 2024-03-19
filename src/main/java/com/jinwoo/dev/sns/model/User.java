package com.jinwoo.dev.sns.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jinwoo.dev.sns.model.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.bytebuddy.agent.builder.AgentBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements UserDetails {

    private Integer id;
    private String username;
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
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.getUserRole().toString()));
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return false;
    }
}
