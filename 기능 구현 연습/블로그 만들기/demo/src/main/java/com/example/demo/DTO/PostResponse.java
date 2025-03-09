package com.example.demo.DTO;

import com.example.demo.entity.PostEntity;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class PostResponse {
    private final String id;
    private final String title;
    private final List<String> images;
    private final String content;
    private final LocalDateTime updatedAt;
    private final String nickname;

    public PostResponse(PostEntity entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.images = entity.getImages();
        this.content = entity.getContent();
        this.updatedAt = entity.getUpdatedAt();
        this.nickname = entity.getNickname();
    }
}
