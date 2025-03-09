package com.example.demo.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor // 기본 생성자 (매개변수 없는 생성자) 자동 추가
@AllArgsConstructor // 모든 필드를 받는 생성자 자동 추가
@Builder
@Document(collection = "User")
public class UserEntity implements UserDetails { // UserDetails 상속받아 인증 객체로 사용
    @Id
    private String id;

    @Indexed(unique = true) // 🔥 email 중복 방지
    private String email;

    @Indexed(unique = true) // 🔥 nickname 중복 방지
    private String nickname;
    private String password;

    @Override // 권한 반환
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if ("skcy151515@gmail.com".equals(this.email)) { // 특정 이메일 체크
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override // 사용자의 id를 반환 (고유한 값)
    public String getUsername() {
        return email;
    }

    @Override // 사용자의 패스워드를 반환 (고유한 값)
    public String getPassword() {
        return password;
    }

    @Override // 계정 만료 여부 반환
    public boolean isAccountNonExpired() {
        // 확인 로직
        return true; // true -> 만료되지 않음
    }

    @Override // 계정 잠금 여부 반환
    public boolean isAccountNonLocked() {
        // 확인 로직
        return true; // true -> 잠금되지 않음
    }

    @Override // 패스워드 만료 여부 반환
    public boolean isCredentialsNonExpired() {
        // 확인 로직
        return true; // true -> 만료되지 않음
    }

    @Override // 계정 사용 가능 여부 반환
    public boolean isEnabled() {
        // 확인 로직
        return true; // true -> 사용 가능
    }
}
