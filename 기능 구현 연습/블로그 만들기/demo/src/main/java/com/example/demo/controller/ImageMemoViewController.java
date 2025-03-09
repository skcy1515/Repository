package com.example.demo.controller;

import com.example.demo.service.ImageMemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class ImageMemoViewController {

    private final ImageMemoService imageMemoService;

    // 파일 업로드 요청 (MultipartFile 사용)
    @PostMapping("/imageMemoPost")
    public String saveImageMemo(@RequestParam("file") MultipartFile file,
                                @RequestParam("comment") String comment) throws IOException {

        imageMemoService.saveImageMemo(file, comment);
        return "redirect:/getAllImageMemo"; // 저장 후 목록 페이지로 이동
    }

    @GetMapping("/getAllImageMemo")
    public String getAllImageMemo(Model model) {
        model.addAttribute("imageMemoResponses", imageMemoService.getAllImageMemos());
        return "imageMemo";
    }

    @PostMapping("/imageMemoDelete")
    public ResponseEntity<String> deleteImage(@RequestParam("imageId") String id) {
        try {
            imageMemoService.deleteImageMemo(id);
            return ResponseEntity.ok("이미지가 삭제되었습니다.");
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }

    @PostMapping("/imageMemoDeletes")
    public ResponseEntity<String> deleteAllImageMemos() {
        try {
            imageMemoService.deleteAllImageMemos();
            return ResponseEntity.ok("모든 이미지가 삭제되었습니다.");
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }
}
