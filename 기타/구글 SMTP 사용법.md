# Spring Boot에서 Gmail SMTP를 사용하여 이메일 보내는 방법
## 1. spring-boot-starter-mail 의존성 추가
```
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-mail'
}
```

## 2. Gmail SMTP 설정 (application.yml)
```
spring:
  mail:
    host: smtp.gmail.com
    port: 587
    username: your-email@gmail.com  # Gmail 계정
    password: your-app-password  # 앱 비밀번호 사용 (띄어쓰기 X)
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
```

## 3. JavaMailSender 사용 예시
```
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
```
