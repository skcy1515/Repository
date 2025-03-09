package com.example.demo.service;

import com.example.demo.entity.UserEntity;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor // final 붙은 변수 생성자 주입 자동 적용
public class UserService {

    private final UserRepository userRepository;
    private final JavaMailSender mailSender;
    private final Map<String, String> verificationCodes = new HashMap<>();
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

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

    // 유저 정보 저장
    public void saveUser(String email, String password, String nickname) {
        // 닉네임 중복 검사
        Optional<UserEntity> existingUser = userRepository.findByNickname(nickname);
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 닉네임입니다!");
        }

        UserEntity userEntity = UserEntity.builder().
                password(bCryptPasswordEncoder.encode(password)).
                email(email).
                nickname(nickname).
                build();
        userRepository.save(userEntity);
    }

}
