package com.example.demo.config;

import com.example.demo.service.UserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final UserDetailService userDetailService;

    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring()
                .requestMatchers(new AntPathRequestMatcher("/static/**"));
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                // 인증(Authorization) 설정
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/signUpSMTP", "/signUpSMTP.js", "/verify-code", "/registerSMTP", "/send-verification") // 이 경로들은 누구나 접근 가능
                        .permitAll()
                        .anyRequest().authenticated()) // 그 외의 모든 요청은 인증이 필요함

                // 로그인(Form Login) 설정
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")  // 로그인 페이지 URL 설정
                        .usernameParameter("email")   // 기본 username 대신 email을 사용
                        .defaultSuccessUrl("/", true)) // 로그인 성공 시 이동할 페이지

                // 로그아웃(Logout) 설정
                .logout(logout -> logout
                        .logoutSuccessUrl("/login") // 로그아웃 성공 후 이동할 페이지
                        .invalidateHttpSession(true))  // 로그아웃 시 세션 무효화

                // CSRF 비활성화
                .csrf(AbstractHttpConfigurer::disable)

                .build();
    }

    @Bean // 인증(Authentication) 관리를 담당하는 AuthenticationManager를 빈(Bean)으로 등록
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder, UserDetailService userDetailService) throws Exception {
        // Spring Security에서 기본적으로 제공하는 인증 제공자
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

        // 사용자 정보 조회를 담당할 서비스 설정
        daoAuthenticationProvider.setUserDetailsService(userDetailService);

        // 비밀번호 검증을 위해 BCryptPasswordEncoder 사용
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder);

        // DaoAuthenticationProvider를 사용하여 인증 관리자(AuthenticationManager) 생성
        return new ProviderManager(daoAuthenticationProvider);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
