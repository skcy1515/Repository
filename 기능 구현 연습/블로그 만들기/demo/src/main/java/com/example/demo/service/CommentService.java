package com.example.demo.service;

import com.example.demo.DTO.CommentResponse;
import com.example.demo.entity.CommentEntity;
import com.example.demo.entity.PostEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor // final 붙은 변수 생성자 주입 자동 적용
public class CommentService {

    private final CommentRepository commentRepository;

    // 댓글 전체 조회
    public List<CommentResponse> getAllComments(String id) {
        return commentRepository.findByPostId(id).stream().map(CommentResponse::new).collect(Collectors.toList());
    }

    // 댓글 작성
    public void saveComment(String id, String content) {
        String loggedInNickname = getLoggedInUserNickname();

        CommentEntity commentEntity = CommentEntity.builder()
                .postId(id)  // URL 접근 가능하도록 경로 저장
                .nickname(loggedInNickname)
                .content(content)
                .createdAt(LocalDateTime.now())
                .build();
        commentRepository.save(commentEntity);
    }

    // 댓글 삭제
    public void deleteComment(String id) {
        String loggedInNickname = getLoggedInUserNickname();
        CommentEntity commentEntity = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 댓글을 찾을 수 없습니다."));

        // 닉네임이 다르면 403 Forbidden 반환
        if (!commentEntity.getNickname().equals(loggedInNickname)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "본인이 작성한 댓글만 삭제할 수 있습니다.");
        }

        commentRepository.deleteById(id);
    }

    // 사용자 닉네임 가져옴
    private String getLoggedInUserNickname() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserEntity) {
            return ((UserEntity) principal).getNickname(); // 닉네임 반환
        } else {
            return principal.toString();
        }
    }


}
