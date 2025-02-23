package com.example.demo.service;

import com.example.demo.entity.ImageMemoEntity;
import com.example.demo.entity.PostEntity;
import com.example.demo.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor // final 붙은 변수 생성자 주입 자동 적용
public class PostService {

    private final PostRepository postRepository;

    @Value("${file.upload-dir}") // application.properties 파일에서 경로 가져오기
    private String uploadDir;

    // 글 저장
    // DTO(Data Transfer Object)는 일반적으로 JSON이나 Form 데이터를 받는 데 사용됨
    // MultipartFile을 포함한 요청은 기본적으로 application/json이 아니라 multipart/form-data 방식으로 전송됨
    public void savePost(List<MultipartFile> files, String title, String content) throws IOException {
        List<String> images = new ArrayList<>(); // 저장할 이미지 리스트

        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                continue;  // 빈 파일 무시
            }

            // 파일명 충돌 방지를 위해 UUID 추가
            String uniqueFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            String filePath = Paths.get(uploadDir, uniqueFileName).toString();

            // 파일 저장
            file.transferTo(new File(filePath));

            // ✅ 리스트에 저장할 파일 경로 추가
            images.add("/uploads/" + uniqueFileName);
        }

        // MongoDB에는 저장된 파일 경로만 저장
        PostEntity postEntity = PostEntity.builder()
                .images(images)  // URL 접근 가능하도록 경로 저장
                .title(title)
                .content(content)
                .updatedAt(LocalDateTime.now())
                .build();
        postRepository.save(postEntity);
    }

    // 특정 ID의 글 삭제
    public void deletePost(String id) {
        PostEntity postEntity = postRepository.findById(id).orElseThrow();

        // 저장된 모든 이미지 경로 가져오기
        List<String> imagePaths = postEntity.getImages();

        // 각 이미지 파일 삭제
        for (String imagePath : imagePaths) {
            // 절대 경로 변환
            String absolutePath = Paths.get(uploadDir, new File(imagePath).getName()).toString();
            File file = new File(absolutePath);

            // 파일 삭제
            if (file.exists()) {
                if (file.delete()) {
                    System.out.println("🟢 파일 삭제 성공: " + absolutePath);
                } else {
                    System.out.println("🚨 파일 삭제 실패: " + absolutePath);
                }
            } else {
                System.out.println("❌ 파일이 존재하지 않음: " + absolutePath);
            }
        }

        // DB에서 해당 글 삭제
        postRepository.deleteById(id);
    }
}
