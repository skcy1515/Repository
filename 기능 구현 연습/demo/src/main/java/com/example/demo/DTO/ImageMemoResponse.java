package com.example.demo.DTO;

import com.example.demo.entity.ImageMemoEntity;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ImageMemoResponse {
    private final String id;
    private final String image;
    private final String comment;
    private final LocalDateTime createdAt;

    public ImageMemoResponse(ImageMemoEntity entity) {
        this.id = entity.getId();
        this.image = entity.getImage();
        this.comment = entity.getComment();
        this.createdAt = entity.getCreatedAt();
    }
}
