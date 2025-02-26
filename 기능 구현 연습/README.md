# 기능 구현 연습
스프링 부트로 웹 기능들을 구현해보았습니다.

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

# 구현한 기능들
## 이미지 방명록
![image](https://github.com/user-attachments/assets/8dbb2b04-12bd-40fc-9ee7-a1ba54ac13b7)

1개의 이미지 파일과 코멘트를 업로드하여 이미지 방명록을 남길 수 있다.

### 코드
- `imageMemo.html` : 화면 구현
- `imageMemo.js` : 입력박스 열기 닫기, 방명록 삭제, 방명록 전체 삭제 기능 처리
- `ImageMemoEntity, Service, Repository, Request, Response, ViewController` : CRUD 작업 수행

## 게시글
![image](https://github.com/user-attachments/assets/5947b830-e07f-44df-95ea-f4835db76554)

![image](https://github.com/user-attachments/assets/5636fa77-8d32-4bdf-9aa2-17275ddf6498)

![image](https://github.com/user-attachments/assets/6e48b775-ec48-4326-b17b-508f0c13ec2a)

![image](https://github.com/user-attachments/assets/641e26a1-ab4d-47a4-887e-4b1e08901740)

게시글을 조회, 작성, 수정, 삭제를 할 수 있다.

### 코드
- `post.html` : 게시글 메인 화면 (전체조회) 구현
- `postView.html` : postId를 통한 특정 게시글 조회 화면 구현
- `postWrite.html` : 게시글 작성 화면 구현
- `postEdit.html` : postId를 통한 특정 게시글 수정 화면 구현
- `postView.js` : postId를 통한 특정 게시글의 정보를 가져옴, 게시글 삭제 기능 처리
- `postWrite.js` : 파일 업로드와 파일 목록 업데이트, 게시글 작성 기능 처리
- `postEdit.js` : postId를 통한 특정 게시글의 정보를 가져옴, 기존 업로드된 파일을 가져오면서 파일 목록 업데이트, 게시글 수정 기능 처리
- `PostEntity, Service, Repository, Request, Response, ViewController` : CRUD 작업 수행
