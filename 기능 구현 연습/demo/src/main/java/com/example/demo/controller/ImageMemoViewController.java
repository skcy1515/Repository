package com.example.demo.controller;

import com.example.demo.DTO.ImageMemoResponse;
import com.example.demo.service.ImageMemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ImageMemoViewController {

    private final ImageMemoService imageMemoService;

    // 파일 업로드 요청 (MultipartFile 사용)
    @PostMapping("/imageMemoPost")
    public String saveImageMemo(@RequestParam("file") MultipartFile file,
                                @RequestParam("comment") String comment) {
        try {
            imageMemoService.saveImageMemo(file, comment);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/getAllImageMemo"; // 저장 후 목록 페이지로 이동
    }

    @GetMapping("/getAllImageMemo")
    public String getAllImageMemo(Model model) {
        List<ImageMemoResponse> imageMemoResponses = imageMemoService.getAllImageMemos();
        model.addAttribute("imageMemoResponses", imageMemoResponses); // Thymeleaf에 데이터 전달
        return "imageMemo";
    }

    @PostMapping("/imageMemoDelete")
    public ResponseEntity<String> deleteImage(@RequestParam("imageId") String id) {
        imageMemoService.deleteImageMemo(id);
        return ResponseEntity.ok("이미지가 삭제되었습니다.");
    }

    @PostMapping("/imageMemoDeletes")
    public ResponseEntity<String> deleteAllImageMemos() {
        imageMemoService.deleteAllImageMemos();
        return ResponseEntity.ok("모든 이미지가 삭제되었습니다.");
    }
}
