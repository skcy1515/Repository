package com.example.demo3.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor // 기본 생성자 (매개변수 없는 생성자) 자동 추가
@AllArgsConstructor // 모든 필드를 받는 생성자 자동 추가
@Builder
@Document(collection = "Api")
public class ApiEntity {
    @Id
    private String id;

    private String itemName; // 제품명
    private String efcyQesitm; // 효능
    private String useMethodQesitm; // 사용법
    private String atpnQesitm; // 주의사항
}
