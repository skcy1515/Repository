package com.example.demo3.controller;

import com.example.demo3.dto.ApiResponse;
import com.example.demo3.service.ApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
public class ApiController {

    private final ApiService apiService;


    @GetMapping("/drug-info")
    public Mono<ApiResponse> getDrugInfo() {
        return apiService.getDrugInfo();
    }
}
