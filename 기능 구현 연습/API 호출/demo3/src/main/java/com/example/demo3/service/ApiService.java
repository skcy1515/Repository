package com.example.demo3.service;

import com.example.demo3.dto.ApiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import reactor.core.publisher.Mono;

@Service
public class ApiService {

    private final WebClient webClient;

    @Value("${api.serviceKey}")
    private String serviceKey; // application.properties에 설정된 서비스 키 값을 주입받음

    public ApiService(WebClient.Builder webClientBuilder) {
        // WebClient에서 URI를 빌드할 때 사용하는 팩토리 객체, 이 객체에 기본 URL을 설정
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory("http://apis.data.go.kr/1471000/DrbEasyDrugInfoService");
        // 파라미터 이름은 그대로 두고, 값만 인코딩
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);

        this.webClient = webClientBuilder
                .uriBuilderFactory(factory) // URI 빌더로 factory를 설정
                .build(); // 설정을 마친 WebClient 객체를 생성
    }

    public Mono<ApiResponse> getDrugInfo() {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder // 요청할 URI를 동적으로 설정
                        .path("/getDrbEasyDrugList") // http://apis.data.go.kr/1471000/DrbEasyDrugInfoService/getDrbEasyDrugList
                        .queryParam("ServiceKey", serviceKey)
                        .queryParam("pageNo", "1")
                        .queryParam("numOfRows", "3")
                        .queryParam("type", "json")
                        .build())
                .retrieve() // 실제로 HTTP 요청을 보내고 응답을 받기 위한 메서드
                .bodyToMono(ApiResponse.class); // 전체 응답을 ApiResponse (JSON)으로 받음
    }
}
