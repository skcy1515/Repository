package com.example.demo2.jwt;

import com.example.demo2.service.UserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
// JWT 토큰을 검사하고 유효한 경우 인증 정보를 SecurityContext에 설정하는 Spring Security 필터
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider; // JWT 토큰을 생성하고 검증
    private final UserDetailService userDetailService;

    @Override
    // 모든 요청을 처리하는 메서드. 요청이 들어올 때마다 실행
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = getJwtFromRequest(request); // 요청 헤더에서 토큰을 가져오는 메서드

        if (token != null && jwtTokenProvider.validateToken(token)) {
            String email = jwtTokenProvider.getEmailFromToken(token);

            UserDetails userDetails = userDetailService.loadUserByUsername(email); // 이메일로부터 사용자 정보 가져옴
            // 인증 객체 생성 (사용자 정보, 비밀번호 (따로 전달하지 않음), 사용자 권한)
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            // 인증 객체에 요청의 세부 정보를 설정
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            // Spring Security의 보안 컨텍스트에 인증 객체 설정
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        // 요청과 응답을 필터 체인의 다음 필터로 넘김
        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization"); // Authorization 헤더에서 JWT 토큰을 추출
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) { // Bearer 라는 접두사를 제거하고 나머지 토큰을 반환
            return bearerToken.substring(7);
        }
        return null;
    }
}
