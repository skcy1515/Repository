package com.example.demo.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor // 기본 생성자 (매개변수 없는 생성자) 자동 추가
@AllArgsConstructor // 모든 필드를 받는 생성자 자동 추가
@Builder
@Document(collection = "imageMemo")  // MongoDB의 imageMemo 컬렉션과 연결
public class ImageMemoEntity {
    @Id
    private String id;
    private String image;
    private String comment;
    private LocalDateTime createdAt;
}
