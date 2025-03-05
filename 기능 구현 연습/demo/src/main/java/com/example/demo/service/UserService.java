package com.example.demo.service;

import com.example.demo.entity.UserEntity;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor // final 붙은 변수 생성자 주입 자동 적용
public class UserService {

    private final UserRepository userRepository;
    private final JavaMailSender mailSender;
    private final Map<String, String> verificationCodes = new HashMap<>();

    // 6자리 인증번호 생성
    private String generateVerificationCode() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000)); // 6자리 숫자
    }

    // 이메일로 인증번호 전송
    public void sendVerificationEmail(String toEmail) {
        String verificationCode = generateVerificationCode();
        verificationCodes.put(toEmail, verificationCode);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("회원가입 인증번호");
        message.setText("인증번호: " + verificationCode);

        mailSender.send(message);
    }

    // 인증번호 확인
    public boolean verifyCode(String email, String code) {
        // verificationCodes(Map)에 해당 이메일이 저장되어 있는지 확인
        // 저장된 인증번호가 사용자가 입력한 인증번호와 일치하는지 확인
        return verificationCodes.containsKey(email) && verificationCodes.get(email).equals(code);
    }

    public void saveUser(String email, String password) {
        UserEntity userEntity = UserEntity.builder().
                password(password).
                email(email).
                build();
        userRepository.save(userEntity);
    }

}
