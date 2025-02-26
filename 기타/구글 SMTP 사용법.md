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
