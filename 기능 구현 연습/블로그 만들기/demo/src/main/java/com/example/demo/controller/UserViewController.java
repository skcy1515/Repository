package com.example.demo.controller;

import com.example.demo.DTO.UserRequest;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequiredArgsConstructor
public class UserViewController {

    private final UserService userService;

    // 렌더링
    @GetMapping("/signUpSMTP")
    public String signUpSMTP() {
        return "signUpSMTP";
    }

    // 렌더링
    @GetMapping("/login")
    public String signIn() {
        return "login";
    }

    // 렌더링
    @GetMapping("/logout")
    public String logout() {
        return "logout";
    }

    // 이메일로 인증번호 전송
    @PostMapping ("/send-verification")
    public ResponseEntity<String> sendVerification(@RequestParam String email) {
        userService.sendVerificationEmail(email);
        return ResponseEntity.ok("인증번호가 이메일로 전송되었습니다.");
    }

    // 인증번호 확인
    @PostMapping("/verify-code")
    public ResponseEntity<String> verifyCode(@RequestParam String email, @RequestParam String code) {
        if (userService.verifyCode(email, code)) {
            return ResponseEntity.ok("이메일 인증 완료");
        } else {
            return ResponseEntity.badRequest().body("인증번호가 올바르지 않습니다.");
        }
    }

    // 회원가입 처리
    // @RequestBody를 사용하여 JSON 데이터를 받기
    @PostMapping("/registerSMTP")
    public ResponseEntity<String> register(@RequestBody UserRequest userRequest) {
        try {
            userService.saveUser(userRequest.getEmail(), userRequest.getPassword(), userRequest.getNickname());
            return ResponseEntity.ok("회원가입이 완료되었습니다.");
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }
}
