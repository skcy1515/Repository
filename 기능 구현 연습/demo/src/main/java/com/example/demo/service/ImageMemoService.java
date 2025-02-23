package com.example.demo.service;

import com.example.demo.DTO.ImageMemoResponse;
import com.example.demo.entity.ImageMemoEntity;
import com.example.demo.repository.ImageMemoRepository;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor // final 붙은 변수 생성자 주입 자동 적용
public class ImageMemoService {

    private final ImageMemoRepository imageMemoRepository;

    @Value("${file.upload-dir}") // application.properties 파일에서 경로 가져오기
    private String uploadDir;

    // 이미지 저장
    // DTO(Data Transfer Object)는 일반적으로 JSON이나 Form 데이터를 받는 데 사용됨
    // MultipartFile을 포함한 요청은 기본적으로 application/json이 아니라 multipart/form-data 방식으로 전송됨
    public void saveImageMemo(MultipartFile file, String comment) throws IOException {
        // 파일이 없는 경우 처리 (파일 저장 로직을 건너뛰고 댓글만 저장)
        if (file == null || file.isEmpty()) {
            ImageMemoEntity imageMemoEntity = ImageMemoEntity.builder()
                    .image("")  // 파일이 없으므로 null 저장
                    .comment(comment)
                    .createdAt(LocalDateTime.now())
                    .build();
            imageMemoRepository.save(imageMemoEntity);
            return;
        }

        // 파일명 충돌 방지를 위해 UUID 추가
        String uniqueFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        String filePath = Paths.get(uploadDir, uniqueFileName).toString();

        // 파일 저장
        file.transferTo(new File(filePath));

        // 저장할 파일 경로 추가
        String imageMemo = "/uploads/" + uniqueFileName;

        // MongoDB에는 저장된 파일 경로만 저장
        ImageMemoEntity imageMemoEntity = ImageMemoEntity.builder()
                .image(imageMemo)  // URL 접근 가능하도록 경로 저장
                .comment(comment)
                .createdAt(LocalDateTime.now())
                .build();
        imageMemoRepository.save(imageMemoEntity);
    }

    // 모든 이미지 조회
    // 스트림 -> ImageMemoEntity 객체를 ImageMemoResponse 객체로 변환
    // new ImageMemoResponse(ImageMemoEntity entity)와 동일
    public List<ImageMemoResponse> getAllImageMemos() {
        List<ImageMemoEntity> imageMemoEntities = imageMemoRepository.findAll();

        return imageMemoEntities.stream()
                .map(ImageMemoResponse::new)
                .collect(Collectors.toList()); // 변환된 ImageMemoResponse 객체들을 다시 리스트로 수집
    }

    // 특정 ID의 이미지 삭제
    public void deleteImageMemo(String id) {
        ImageMemoEntity imageMemoEntity = imageMemoRepository.findById(id).orElseThrow();

        // 이미지 파일 삭제
        deleteFile(imageMemoEntity.getImage());

        // ✅ DB에서 해당 이미지 데이터 삭제
        imageMemoRepository.deleteById(id);
    }

    // 모든 이미지 삭제
    public void deleteAllImageMemos() {
        List<ImageMemoEntity> allImages = imageMemoRepository.findAll();

        // ✅ 모든 파일 삭제
        for (ImageMemoEntity image : allImages) {
            deleteFile(image.getImage());
        }

        imageMemoRepository.deleteAll();
    }

    // 파일 삭제 메서드 (공통 사용)
    private void deleteFile(String imagePath) {
        if (imagePath != null && !imagePath.isEmpty()) {
            // 절대 경로 변환
            String absolutePath = Paths.get(uploadDir, new File(imagePath).getName()).toString();
            File file = new File(absolutePath);

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
    }
}
