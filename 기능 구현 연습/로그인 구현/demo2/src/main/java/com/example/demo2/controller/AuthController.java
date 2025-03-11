package com.example.demo2.controller;

import com.example.demo2.dto.AccessTokenResponse;
import com.example.demo2.dto.AuthResponse;
import com.example.demo2.dto.UserRequest;
import com.example.demo2.jwt.JwtTokenProvider;
import com.example.demo2.service.AuthService;
import com.example.demo2.service.UserDetailService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider; // JWT 토큰을 생성하고 검증하는 유틸리티 클래스
    private final UserDetailService userDetailService; // 사용자 정보를 불러오는 서비스
    private final AuthService authService; // 사용자 인증을 처리하는 서비스

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody UserRequest loginRequest) {
        AuthResponse authResponse = authService.authenticateUser(loginRequest.getEmail(), loginRequest.getPassword());

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", authResponse.getRefreshToken())
                .httpOnly(true) // JavaScript로 접근할 수 없도록 설정
                // HTTPS에서만 쿠키가 전송되도록 설정
                // .secure(true)
                .sameSite("Strict") // CSRF 공격 방지
                .path("/") // 쿠키의 유효 경로
                .maxAge(jwtTokenProvider.getRefreshTokenExpiration() / 1000) // 쿠키의 만료 시간 설정 (초 단위)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(new AccessTokenResponse(authResponse.getAccessToken()));
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(@CookieValue(value = "refreshToken", required = false) String refreshToken) {
        if (jwtTokenProvider.validateToken(refreshToken)) { // 전달받은 리프레시 토큰이 유효한지 검증
            String email = jwtTokenProvider.getEmailFromToken(refreshToken); // 리프레시 토큰으로부터 이메일을 추출

            String newAccessToken = jwtTokenProvider.generateAccessToken(email); // 액세스 토큰 생성
            return ResponseEntity.ok(new AccessTokenResponse(newAccessToken));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("리프레시 토큰이 만료되었습니다.");
        }
    }

    // 회원가입 요청
    @PostMapping("/signUp")
    public ResponseEntity<String> signUp(@RequestBody UserRequest userRequest) {
        try {
            authService.signUp(userRequest.getEmail(), userRequest.getPassword());
            return ResponseEntity.ok("회원가입이 완료되었습니다.");
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {
        // 쿠키 삭제: 'refreshToken' 쿠키를 만료시킨다.
        Cookie cookie = new Cookie("refreshToken", null);  // 값은 null로 설정
        cookie.setPath("/");  // 쿠키 경로 설정 (로그인 시 사용한 경로와 동일해야 함)
        cookie.setMaxAge(0);  // 쿠키 만료 시간 설정 (0으로 설정하면 삭제됨)

        // 쿠키를 클라이언트에 전송하여 삭제
        response.addCookie(cookie);

        return ResponseEntity.ok("로그아웃 되었습니다.");
    }

    // 인증 요청
    @GetMapping("/api/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("인증 성공.");
    }
}
