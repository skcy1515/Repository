package com.example.demo.controller;

import com.example.demo.DTO.CommentResponse;
import com.example.demo.DTO.PostResponse;
import com.example.demo.service.CommentService;
import com.example.demo.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class PostViewController {

    private final PostService postService;
    private final CommentService commentService;

    // 게시글 전체 조회
    @GetMapping("/getAllPosts")
    public String getAllPosts(Model model) {
        model.addAttribute("postResponses", postService.getAllPosts());
        return "post";
    }

    // 페이지 렌더링
    @GetMapping("/postWrite")
    public String postWrite() {
        return "postWrite";  // postWrite.html을 반환
    }

    // 게시글 업로드
    @PostMapping("/postUpload")
    public ResponseEntity<String> postUpload(@RequestParam("title") String title,
                                             @RequestParam("content") String content,
                                             @RequestPart(value = "files", required = false) List<MultipartFile> files) throws IOException {
        if (files == null || files.isEmpty()) {
            files = new ArrayList<>(); // 빈 리스트로 초기화
        }

        postService.savePost(files, title, content);
        return ResponseEntity.ok("게시글이 저장되었습니다.");
    }

    // postView 렌더링
    @GetMapping("/postView/{id}")
    public String postView() {
        return "postView";  // postView.html 렌더링
    }

    // 특정 게시물 조회 api
    @GetMapping("/api/postView/{id}")
    public ResponseEntity<PostResponse> postViewApi(@PathVariable String id) {
        PostResponse postResponse = postService.getPostById(id);

        return ResponseEntity.ok(postResponse);
    }

    // 특정 게시물의 댓글 조회 api
    @GetMapping("/api/postViewComments/{id}")
    public ResponseEntity<List<CommentResponse>> CommentsViewApi(@PathVariable String id) {
        List<CommentResponse> commentResponses = commentService.getAllComments(id);

        return ResponseEntity.ok(commentResponses);
    }

    // 댓글 작성
    @PostMapping("/api/UploadComment")
    public ResponseEntity<String> postUpload(@RequestParam("content") String content, @RequestParam("postId") String id) {

        commentService.saveComment(id, content);
        return ResponseEntity.ok("댓글이 저장되었습니다.");
    }

    // 댓글 삭제
    @PostMapping("/api/DeleteComment")
    public ResponseEntity<String> DeleteComment(@RequestParam("commentId") String id) {
        try {
            commentService.deleteComment(id);
            return ResponseEntity.ok("댓글이 삭제되었습니다.");
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }

    // 특정 게시물 조회 (수정) api
    @GetMapping("/api/postViewEdit/{id}")
    public ResponseEntity<?> postViewEditApi(@PathVariable String id) {
        try {
            PostResponse postResponse = postService.getPostByIdEdit(id);
            return ResponseEntity.ok(postResponse);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }

    // 특정 게시물 삭제 api
    @PostMapping("/api/postViewDelete/{id}")
    public ResponseEntity<String> postViewDeleteApi(@PathVariable String id) {
        try {
            postService.deletePost(id);
            return ResponseEntity.ok("삭제되었습니다.");
        }
        catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }

    // 게시글 수정 렌더링
    @GetMapping("/postViewUpdate/{id}")
    public String postViewUpdate() {
        return "postEdit";
    }

    // 게시글 수정
    @PostMapping("/api/postViewUpdate/{id}")
    public ResponseEntity<String> postUpdate(@PathVariable String id,
                                             @RequestParam("title") String title,
                                             @RequestParam("content") String content,
                                             @RequestParam(value = "existingImages", required = false) String existingImagesJson,
                                             @RequestParam(value = "deleteFiles", required = false) String deleteFilesJson,
                                             @RequestPart(value = "files", required = false) List<MultipartFile> files) throws IOException {
        if (files == null || files.isEmpty()) {
            files = new ArrayList<>(); // 빈 리스트로 초기화
        }
        try {
            postService.updatePost(id, files, title, content, existingImagesJson, deleteFilesJson);
            return ResponseEntity.ok("게시글이 수정되었습니다.");
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }
}
