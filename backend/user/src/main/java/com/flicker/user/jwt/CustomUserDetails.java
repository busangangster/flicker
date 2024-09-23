package com.flicker.user.jwt;

import com.flicker.user.user.domain.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;

// User 엔티티는 프로젝트에서 사용하는 실제 사용자 정보를 담고 있는 클래스입니다.
public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 권한을 반환합니다. 여기서는 간단히 'ROLE_USER' 권한을 반환하는 예입니다.
        return Collections.singleton(() -> "ROLE_USER");
    }

    public Long getUserSeq(){
        return user.getUserSeq();
    }

    public LocalDate getBirthDate(){
        return user.getUserInfo().getBirthDate();
    }

    public Character getGender(){
        return user.getUserInfo().getGender();
    }

    public String getNickname(){
        return user.getNickname();
    }

    public String getEmail(){
        return user.getEmail();
    }

    public String getProfilePhotoUrl(){
        return user.getProfilePhotoUrl();
    }

    public String getUserId(){
        return user.getUserId();
    }

    @Override
    public String getPassword() {
        // User 엔티티에서 비밀번호를 반환
        return user.getHashedPass();
    }

    @Override
    public String getUsername() {
        // User 엔티티에서 사용자 이름을 반환
        return user.getUserId();
    }

    @Override
    public boolean isAccountNonExpired() {
        // 계정 만료 여부 반환
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // 계정 잠금 여부 반환
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // 비밀번호 만료 여부 반환
        return true;
    }

    @Override
    public boolean isEnabled() {
        // 계정 활성화 여부 반환
        return user.getIsActive() == 1;
    }
}
