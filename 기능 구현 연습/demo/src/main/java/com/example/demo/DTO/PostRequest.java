package com.example.demo.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PostRequest {
    private String title;
    private List<String> images;
    private String content;
    private LocalDateTime updatedAt;
}
