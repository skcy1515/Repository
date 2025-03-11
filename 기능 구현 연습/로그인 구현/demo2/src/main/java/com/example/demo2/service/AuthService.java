package com.example.demo2.service;

import com.example.demo2.dto.AuthResponse;
import com.example.demo2.entity.UserEntity;
import com.example.demo2.jwt.JwtTokenProvider;
import com.example.demo2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    // 로그인 요청만 수행
    public AuthResponse authenticateUser(String email, String password) {
        // 전달된 이메일과 비밀번호로 인증 수행
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password));

        // Spring Security의 SecurityContext 에 인증 정보를 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtTokenProvider.generateAccessToken(email);
        String refreshToken = jwtTokenProvider.generateRefreshToken(email);

        return new AuthResponse(accessToken, refreshToken);
    }

    // 회원가입 메서드
    public void signUp(String email, String password) {
        Optional<UserEntity> existingEmail = userRepository.findByEmail(email);
        if (existingEmail.isPresent()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "이미 존재하는 이메일입니다.");
        }

        UserEntity userEntity = UserEntity.builder().
                password(bCryptPasswordEncoder.encode(password)).
                email(email).
                build();
        userRepository.save(userEntity);
    }
}
