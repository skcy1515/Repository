package com.example.demo3.service;

import com.example.demo3.repository.ApiRepository;
import com.example.demo3.repository.ApiRepository2;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class MongoService {
    private final ApiRepository apiRepository;
    private final ApiRepository2 apiRepository2;

    public Mono<Void> syncEngNames() {
        return apiRepository.findByEngNameIsNull() // null인 애들만 대상으로 동기화
                .flatMap(api -> apiRepository2.findAll()
                        .filter(api2 -> api2.getITEM_NAME().equals(api.getItemName()))
                        .next()
                        .flatMap(matchedApi2 -> {
                            api.setEngName(matchedApi2.getITEM_ENG_NAME());
                            return apiRepository.save(api);
                        })
                        .switchIfEmpty(Mono.just(api)) // 매칭 안되면 그냥 넘어감
                )
                .then() // 최종 Mono<Void> 리턴
                .doFinally(signal -> System.out.println("✅ 영문 이름 동기화 작업 완료"));
    }
}
