# 블로그 기능 구현
스프링 부트로 이미지 방명록, 블로그 기능 구현

[코드](https://github.com/skcy1515/Repository/tree/main/%EA%B8%B0%EB%8A%A5%20%EA%B5%AC%ED%98%84%20%EC%97%B0%EC%8A%B5/%EB%B8%94%EB%A1%9C%EA%B7%B8%20%EB%A7%8C%EB%93%A4%EA%B8%B0/demo/src/main/java/com/example/demo)

# 사용한 기술
- 스프링부트
- 타임리프
- 제이쿼리
- 부트스트랩

# 기본 설정
```
// 업로드된 파일을 정적 리소스로 제공
@Configuration
public class WebConfig implements WebMvcConfigurer {
/*    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**") // 브라우저에서 /uploads/파일명 요청을 처리하도록 설정
                .addResourceLocations("file:/home/ubuntu/Tests/uploads/"); // 실제 파일이 저장된 디렉토리를 매핑
    }*/

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**") // 웹에서 /uploads/로 접근 가능
                .addResourceLocations("file:C:/Users/skcy1/OneDrive/Desktop/uploads");  // 로컬 폴더와 매핑
    }

}
```
- `WebConfig` : Spring Boot에서 업로드된 파일을 정적 리소스로 제공하기 위한 설정이다. 즉, 웹 브라우저에서 /uploads/파일명 형태의 요청을 보내면, 서버의 특정 디렉토리에 저장된 파일을 제공할 수 있도록 매핑한다.

```
spring:
  data:
    mongodb:
      uri: mongodb+srv://<아이디>:<비밀번호>@<클러스터이름>.<유니크>.mongodb.net/<사용할데이터베이스이름>?retryWrites=true&w=majority&appName=Cluster0&tlsAllowInvalidCertificates=true
```
- `application.yml` : MongoDB 기본 설정 파일

```
file.upload-dir=default
spring.profiles.active=dev

spring.servlet.multipart.max-file-size=50MB
spring.servlet.multipart.max-request-size=50MB
```
- `application.properties`: 업로드 경로 지정 (dev: 윈도우, prod: 리눅스), 파일 업로드 최대 용량 설정

```
file.upload-dir=C:/Users/skcy1/OneDrive/Desktop/uploads
file.upload-dir=/home/ubuntu/Tests/uploads/
```
- `application-dev.properties`, `application-prod.properties`: 윈도우, 리눅스 경로 설정

# 구현한 기능들
## 로그인, 회원가입, 로그아웃
![image](https://github.com/user-attachments/assets/ffd00bf6-58d0-4151-8fa0-cfda1f7d1104)

![image](https://github.com/user-attachments/assets/0a71eb2f-7e40-4fe9-9910-0cfdea550e46)

![image](https://github.com/user-attachments/assets/64fcd22c-bc4d-4e55-bb64-f2f29ce7f29a)

- 로그인, 회원가입, 로그아웃을 할 수 있다.
- 로그인은 스프링 시큐리티를 사용
- 회원가입은 자바 이메일 센더를 이용하여 이메일 인증으로 기능

## 이미지 방명록
![image](https://github.com/user-attachments/assets/8dbb2b04-12bd-40fc-9ee7-a1ba54ac13b7)

1개의 이미지 파일과 코멘트를 업로드하여 이미지 방명록을 남길 수 있다.

## 게시글
![image](https://github.com/user-attachments/assets/5947b830-e07f-44df-95ea-f4835db76554)

![image](https://github.com/user-attachments/assets/5636fa77-8d32-4bdf-9aa2-17275ddf6498)

![image](https://github.com/user-attachments/assets/6e48b775-ec48-4326-b17b-508f0c13ec2a)

![image](https://github.com/user-attachments/assets/641e26a1-ab4d-47a4-887e-4b1e08901740)

게시글을 조회, 작성, 수정, 삭제를 할 수 있다.

## 댓글
![image](https://github.com/user-attachments/assets/dd004cf2-e008-4558-b87c-61cd9d8ec996)

댓글을 조회, 작성, 삭제할 수 있다.
