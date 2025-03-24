package com.example.demo3.service;

import com.example.demo3.dto.ApiResponse;
import com.example.demo3.entity.ApiEntity;
import com.example.demo3.repository.ApiRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;

@Service
public class ApiService {

    private final WebClient webClient;
    private final ApiRepository apiRepository;

    @Value("${api.serviceKey}")
    private String serviceKey; // application.properties에 설정된 서비스 키 값을 주입받음

    public ApiService(WebClient.Builder webClientBuilder, ApiRepository apiRepository) {
        this.webClient = webClientBuilder
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer -> configurer
                                .defaultCodecs()
                                .maxInMemorySize(10 * 1024 * 1024)) // 최대 10MB까지 메모리에 읽을 수 있게 설정
                        .build())
                .build();
        this.apiRepository = apiRepository;
    }

    // 공공 API에서 의약품 정보를 가져와서 MongoDB에 저장하는 비동기 로직
    public Mono<Void> getDrugInfoAndSave(String pageNo) {
        // API 요청을 보낼 때 사용할 URI를 만드는 부분
        URI uri = UriComponentsBuilder
                .fromUriString("http://apis.data.go.kr/1471000/DrbEasyDrugInfoService/getDrbEasyDrugList")
                .queryParam("ServiceKey", serviceKey)
                .queryParam("pageNo", pageNo)
                .queryParam("numOfRows", "100")
                .queryParam("type", "json")
                .build(true) // 여기 true는 인코딩된 URI로 빌드
                .toUri();

        System.out.println("최종 요청 URI: " + uri); // ✅ 여기서 출력

        // WebClient를 이용해서 해당 URL로 HTTP GET 요청을 보냄
        return webClient.get()
                .uri(uri)
                .retrieve()
                // bodyToMono()는 비동기적으로 결과를 하나 받을 때 사용함 (Mono<ApiResponse> 반환)
                // 응답은 JSON이니까 ApiResponse.class로 파싱
                .bodyToMono(ApiResponse.class)
                // 응답 받은 ApiResponse를 이용해서 다음 작업(변환 + 저장)을 수행
                // .flatMap()은 변환 + 비동기 호출 포함된 작업에 사용
                .flatMap(apiResponse -> {
                    // 응답 본문에서 실제 데이터(items)를 꺼내고 각각을 ApiEntity로 변환
                    List<ApiEntity> entities = apiResponse.getBody().getItems().stream()
                            .map(item -> ApiEntity.builder()
                                    .itemName(item.getItemName())
                                    .efcyQesitm(item.getEfcyQesitm())
                                    .useMethodQesitm(item.getUseMethodQesitm())
                                    .atpnQesitm(item.getAtpnQesitm())
                                    .seQesitm(item.getSeQesitm())
                                    .build())
                            .toList();

                    // 변환된 엔티티 리스트를 MongoDB에 저장 (saveAll)
                    // .then()을 쓰면 저장이 끝났다는 신호만 리턴함 (Mono<Void>)
                    return apiRepository.saveAll(entities).then();
                });
    }

    public void DeleteAllDrugs() {
        apiRepository.deleteAll().subscribe();
    }
}
