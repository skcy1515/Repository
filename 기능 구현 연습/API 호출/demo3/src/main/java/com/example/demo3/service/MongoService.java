package com.example.demo3.service;

import com.example.demo3.entity.ApiEntity2;
import com.example.demo3.repository.ApiRepository;
import com.example.demo3.repository.ApiRepository2;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class MongoService {
    private final ApiRepository apiRepository;
    private final ApiRepository2 apiRepository2;

    public Mono<Void> syncEngNames() {
        // 1. 먼저 ApiEntity2를 Map으로 캐싱
        Mono<Map<String, String>> api2MapMono = apiRepository2.findAll()
                .collectMap(ApiEntity2::getITEM_NAME, ApiEntity2::getITEM_ENG_NAME);

        // 2. ApiEntity 중 engName이 null인 것만 가져와서 동기화
        return api2MapMono.flatMapMany(api2Map ->
                        apiRepository.findByEngNameIsNull()
                                .flatMap(api -> {
                                    String engName = api2Map.get(api.getItemName());
                                    if (engName != null) {
                                        api.setEngName(engName);
                                        return apiRepository.save(api);
                                    } else {
                                        return Mono.empty(); // 매칭 안 되면 저장 안 함
                                    }
                                })
                ).then()
                .doFinally(signal -> System.out.println("✅ 영문 이름 동기화 작업 완료"));
    }
}
