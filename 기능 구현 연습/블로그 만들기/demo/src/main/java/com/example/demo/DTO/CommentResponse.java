package com.example.demo.DTO;

import com.example.demo.entity.CommentEntity;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponse {
    private final String id;
    private final String postId;
    private final String content;
    private final LocalDateTime createdAt;
    private final String nickname;

    public CommentResponse(CommentEntity entity) {
        this.id = entity.getId();
        this.postId = entity.getPostId();
        this.content = entity.getContent();
        this.createdAt = entity.getCreatedAt();
        this.nickname = entity.getNickname();
    }
}
