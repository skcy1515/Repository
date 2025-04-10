package com.example.demo3.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor // 기본 생성자 (매개변수 없는 생성자) 자동 추가
@AllArgsConstructor // 모든 필드를 받는 생성자 자동 추가
@Builder
@Document(collection = "Api2")
public class ApiEntity2 {
    @Id
    private String id;

    private String ITEM_NAME; // 제품명
    private String ITEM_ENG_NAME;
}
