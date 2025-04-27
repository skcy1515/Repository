package com.example.demo3.controller;

import com.example.demo3.service.ApiService;
import com.example.demo3.service.MongoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@RestController
public class ApiController {

    private final ApiService apiService;
    private final MongoService mongoService;

    @GetMapping("/drug-info")
    public Mono<String> getDrugInfoAllPages() {
        // 1부터 48까지의 숫자를 스트림으로 생성한 뒤 각 숫자를 String으로 변환하고 리스트로 만든다.
        List<String> pageList = IntStream.rangeClosed(1, 48)
                .mapToObj(String::valueOf)
                .toList();

        // 방금 만든 pageList를 기반으로 Flux를 만든다.
        Flux.fromIterable(pageList)
                // 각 페이지 번호(String)에 대해 apiService.getDrugInfoAndSave()를 실행한다.
                //concatMap()을 사용했기 때문에 순차적으로 처리된다. (즉, 페이지 1 → 페이지 2 → ...)
                .concatMap(apiService::getDrugInfoAndSave)
                .subscribe(); // 백그라운드 쓰레드에서 실행

        // 클라이언트에게는 즉시 응답을 준다.
        // API 데이터 수집 작업은 백그라운드에서 계속 진행되고, 클라이언트는 기다릴 필요가 없다.
        return Mono.just("요청을 수락했음. 백그라운드에서 처리 중.");
    }

    @GetMapping("/drug-info2")
    public Mono<String> getDrugInfoAllPages2() {
        List<String> pageList = IntStream.rangeClosed(451, 453)
                .mapToObj(String::valueOf)
                .toList();

        Flux.fromIterable(pageList)
                .concatMap(page ->
                        apiService.getDrugInfoAndSave2(page)
                                .onErrorResume(e -> {
                                    // 로그 출력하거나 예외 처리
                                    System.err.println("페이지 " + page + " 처리 중 오류 발생: " + e.getMessage());
                                    // 빈 Mono 반환하여 다음 페이지로 계속 진행
                                    return Mono.empty();
                                })
                )
                .subscribe();

        return Mono.just("요청을 수락했음. 백그라운드에서 처리 중.");
    }


    @DeleteMapping("/drug-info")
    public void DeleteAllDrugs() {
        apiService.DeleteAllDrugs();
    }

    @GetMapping("/engName")
    public Mono<String> syncEngNames() {
        // 백그라운드에서 실행되도록 subscribe만 하고
        mongoService.syncEngNames().subscribe();

        // 클라이언트에 바로 응답
        return Mono.just("요청 수락됨. 영문명 동기화는 백그라운드에서 처리 중.");
    }
}
