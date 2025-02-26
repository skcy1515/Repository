package com.example.demo.service;

import com.example.demo.DTO.PostResponse;
import com.example.demo.entity.PostEntity;
import com.example.demo.repository.PostRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

        // 파일이 없거나, 리스트가 비어 있는 경우 이미지 저장 없이 게시글만 저장
        if (files == null || files.isEmpty()) {
            PostEntity postEntity = PostEntity.builder()
                    .title(title)
                    .content(content)
                    .images(Collections.emptyList()) // 빈 리스트 저장 (null 방지)
                    .updatedAt(LocalDateTime.now())
                    .build();
            postRepository.save(postEntity);
            return;
        }

        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                continue;  // 빈 파일 무시
            }

            // 파일명 충돌 방지를 위해 UUID 추가
            String uniqueFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            String filePath = Paths.get(uploadDir, uniqueFileName).toString();

            // 파일 저장
            file.transferTo(new File(filePath));

            // 리스트에 저장할 파일 경로 추가
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

    // 게시글 전체 조회
    public List<PostResponse> getAllPosts() {
        return postRepository.findAll().stream().map(PostResponse::new).collect(Collectors.toList());
    }

    // 특정 게시글 조회
    public PostResponse getPostById(String id) {
        PostEntity postEntity = postRepository.findById(id).orElseThrow();

        return new PostResponse(postEntity);
    }

    // 특정 게시글 수정
    public void updatePost(String postId, List<MultipartFile> files, String title, String content,
                           String existingImagesJson, String deleteFilesJson) throws IOException {

        // 기존 게시글 가져오기
        PostEntity postEntity = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("해당 게시글을 찾을 수 없습니다."));

        // 기존 이미지 목록
        // 유지할 기존 파일 목록을 JSON으로 받아 List<String> 형태로 변환
        // "[\"/uploads/file1.png\", \"/uploads/file2.jpg\"]" 형식을 리스트로 변환
        List<String> existingImages = new ArrayList<>();
        if (existingImagesJson != null && !existingImagesJson.isEmpty()) {
            existingImages = new ObjectMapper().readValue(existingImagesJson, new TypeReference<List<String>>() {});
        }

        // 삭제할 파일 목록 처리 (서버에서 파일 삭제)
        // 위처럼 JSON으로 받아 리스트 형태로 변환
        if (deleteFilesJson != null && !deleteFilesJson.isEmpty()) {
            List<String> deleteFiles = new ObjectMapper().readValue(deleteFilesJson, new TypeReference<List<String>>() {});

            for (String fileUrl : deleteFiles) {
                String absolutePath = Paths.get(uploadDir, new File(fileUrl).getName()).toString();
                File file = new File(absolutePath);

                if (file.exists() && file.delete()) {
                    System.out.println("🟢 파일 삭제됨: " + absolutePath);
                }
            }
        }

        // 새 파일 저장
        List<String> newImages = new ArrayList<>(existingImages); // 기존 유지할 파일 목록을 newImages 리스트에 복사
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    String uniqueFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                    String filePath = Paths.get(uploadDir, uniqueFileName).toString();
                    file.transferTo(new File(filePath));
                    newImages.add("/uploads/" + uniqueFileName);
                }
            }
        }

        // 게시글 수정 및 저장
        postEntity.setTitle(title);
        postEntity.setContent(content);
        postEntity.setImages(newImages);
        postEntity.setUpdatedAt(LocalDateTime.now());

        postRepository.save(postEntity);
    }
}
