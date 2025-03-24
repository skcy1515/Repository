# 공공데이터 API 호출하기
[코드](https://github.com/skcy1515/Repository/tree/main/%EA%B8%B0%EB%8A%A5%20%EA%B5%AC%ED%98%84%20%EC%97%B0%EC%8A%B5/API%20%ED%98%B8%EC%B6%9C/demo3/src/main/java/com/example/demo3)

## 사전 설정
### 필수 의존성
```
implementation 'org.springframework.boot:spring-boot-starter-webflux'
implementation 'org.springframework.boot:spring-boot-starter-data-mongodb-reactive'
```

### application.properties
```
api.serviceKey=YOUR_SERVICE_KEY
```


## 구현한 기능
```
<response>
    <header>
        <resultCode>00</resultCode>
        <resultMsg>NORMAL SERVICE.</resultMsg>
    </header>
    <body>
        <numOfRows>10</numOfRows>
        <pageNo>1</pageNo>
        <totalCount>4742</totalCount>
        <items>
            <item>
                <entpName>동화약품(주)</entpName>
                <itemName>활명수</itemName>
                <itemSeq>195700020</itemSeq>
                <efcyQesitm>이 약은 식욕감퇴(식욕부진), 위부팽만감, 소화불량, 과식, 체함, 구역, 구토에 사용합니다. </efcyQesitm>
                <useMethodQesitm>만 15세 이상 및 성인은 1회 1병(75 mL), 만 11세이상~만 15세미만은 1회 2/3병(50 mL), 만 8세 이상~만 11세 미만은 1회 1/2병(37.5 mL), 만 5세 이상~만 8세 미만은 1회 1/3병(25 mL), 만 3세 이상~만 5세 미만은 1회 1/4병(18.75 mL), 만 1세 이상~만 3세 미만은 1회 1/5병(15 mL), 1일 3회 식후에 복용합니다. 복용간격은 4시간 이상으로 합니다. </useMethodQesitm>
                <atpnWarnQesitm/>
                <atpnQesitm>만 3개월 미만의 젖먹이는 이 약을 복용하지 마십시오. 이 약을 복용하기 전에 만 1세 미만의 젖먹이, 임부 또는 임신하고 있을 가능성이 있는 여성, 카라멜에 과민증 환자 또는 경험자, 나트륨 제한 식이를 하는 사람은 의사 또는 약사와 상의하십시오. 정해진 용법과 용량을 잘 지키십시오. 어린이에게 투여할 경우 보호자의 지도 감독하에 투여하십시오. 1개월 정도 복용하여도 증상의 개선이 없을 경우 복용을 즉각 중지하고 의사 또는 약사와 상의하십시오. </atpnQesitm>
                <intrcQesitm/>
                <seQesitm/>
                <depositMethodQesitm>습기와 빛을 피해 실온에서 보관하십시오. 어린이의 손이 닿지 않는 곳에 보관하십시오. </depositMethodQesitm>
                <openDe>2021-01-29 00:00:00</openDe>
                <updateDe>2024-05-09</updateDe>
                <itemImage/>
                <bizrno>1108100102</bizrno>
            </item>
```
기존 http://apis.data.go.kr/1471000/DrbEasyDrugInfoService/getDrbEasyDrugList?serviceKey=서비스키 들어가서 나오는 내용들을 JSON으로 원하는 값만 가져올 수 있음

```
{
    "body": {
        "pageNo": 1,
        "totalCount": 4742,
        "numOfRows": 3,
        "items": [
            {
                "entpName": "동화약품(주)",
                "itemName": "활명수",
                "efcyQesitm": "이 약은 식욕감퇴(식욕부진), 위부팽만감, 소화불량, 과식, 체함, 구역, 구토에 사용합니다.\n",
                "useMethodQesitm": "만 15세 이상 및 성인은 1회 1병(75 mL), 만 11세이상~만 15세미만은 1회 2/3병(50 mL), 만 8세 이상~만 11세 미만은 1회 1/2병(37.5 mL), 만 5세 이상~만 8세 미만은 1회 1/3병(25 mL), 만 3세 이상~만 5세 미만은 1회 1/4병(18.75 mL), 만 1세 이상~만 3세 미만은 1회 1/5병(15 mL), 1일 3회 식후에 복용합니다. 복용간격은 4시간 이상으로 합니다.\n",
                "atpnQesitm": "만 3개월 미만의 젖먹이는 이 약을 복용하지 마십시오.\n\n이 약을 복용하기 전에 만 1세 미만의 젖먹이, 임부 또는 임신하고 있을 가능성이 있는 여성, 카라멜에 과민증 환자 또는 경험자, 나트륨 제한 식이를 하는 사람은 의사 또는 약사와 상의하십시오.\n\n정해진 용법과 용량을 잘 지키십시오.\n\n어린이에게 투여할 경우 보호자의 지도 감독하에 투여하십시오.\n\n1개월 정도 복용하여도 증상의 개선이 없을 경우 복용을 즉각 중지하고 의사 또는 약사와 상의하십시오.\n",
                "depositMethodQesitm": "습기와 빛을 피해 실온에서 보관하십시오.\n\n어린이의 손이 닿지 않는 곳에 보관하십시오.\n"
            },
            {
                "entpName": "신신제약(주)",
                "itemName": "신신티눈고(살리실산반창고)(수출명:SINSINCORNPLASTER)",
                "efcyQesitm": "이 약은 티눈, 못(굳은살), 사마귀에 사용합니다. \n",
                "useMethodQesitm": "이형지로부터 벗겨 이 약제면을 환부(질환 부위)에 대고 테이프로 고정하고 2~5일마다 교체하여 붙입니다.\n",
                "atpnQesitm": "이 약에 과민증 환자, 당뇨병, 혈액순환장애 환자는 이 약을 사용하지 마십시오.\n\n눈 주위, 점막, 감염, 염증, 발적(충혈되어 붉어짐), 자극이 있는 부위, 점, 태어날 때부터 있는 점 또는 사마귀, 털이 있는 사마귀, 생식기 부위의 사마귀에 사용하지 마십시오.\n\n이 약을 사용하기 전에 영아 및 유아, 약 또는 화장품에 과민증 환자, 신부전 환자, 임부 또는 임신하고 있을 가능성이 있는 여성 및 수유부는 의사 또는 약사와 상의하십시오.\n\n정해진 용법과 용량을 잘 지키십시오.\n\n화농(곪음)성피부염, 습윤(습기 참), 미란(짓무름)의 경우 이를 미리 치료한 후 이 약을 적용하십시오.\n\n이 약을 붙인 채로 물 속에 들어가는 경우에는 약고가 녹아서 없어질 수 있으므로 주의하십시오.\n\n어린이에게 투여할 경우 보호자의 지도 감독하에 투여하십시오.\n\n이 약은 외용으로만 사용하십시오.\n\n눈, 코, 입 및 다른 점막에 닿지 않도록 주의하여 적용하고, 만일 눈에 들어갔을 경우 충분한 양의 물로 완전히 씻어내십시오.\n\n환부(질환 부위) 이외의 피부에 닿지 않도록 주의하십시오.\n",
                "depositMethodQesitm": "습기와 빛을 피해 보관하십시오.\n\n어린이의 손이 닿지 않는 곳에 보관하십시오.\n"
            },
            {
                "entpName": "삼진제약(주)",
                "itemName": "아네모정",
                "efcyQesitm": "이 약은 위산과다, 속쓰림, 위부불쾌감, 위부팽만감, 체함, 구역, 구토, 위통, 신트림에 사용합니다.",
                "useMethodQesitm": "성인 1회 2정, 1일 3회 식간(식사와 식사때 사이) 및 취침시에 복용합니다.",
                "atpnQesitm": "투석요법을 받고 있는 환자, 수유부, 만 7세 이하의 어린이, 갈락토오스 불내성, Lapp 유당분해효소 결핍증 또는 포도당-갈락토오스 흡수장애 등의 유전적인 문제가 있는 환자는 이 약을 복용하지 마십시오.이 약을 복용하기 전에 이 약에 과민증 환자, 알레르기 체질, 알레르기 증상(발진, 충혈되어 붉어짐, 가려움 등) 경험자, 신장장애 환자, 임부 또는 임신하고 있을 가능성이 있는 여성, 나트륨 제한 식이를 하는 사람은 의사 또는 약사와 상의하십시오.정해진 용법과 용량을 잘 지키십시오.",
                "depositMethodQesitm": "습기와 빛을 피해 보관하십시오.어린이의 손이 닿지 않는 곳에 보관하십시오."
            }
        ]
    }
}
```
# 코드
## WebClientConfig
```
@Configuration
public class WebClientConfig {

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder(); // WebClient 빌더를 빈으로 등록
    }
}
```

## ApiResponse
```
@Getter
@AllArgsConstructor
public class ApiResponse {
    private Body body;

    @Getter
    @AllArgsConstructor
    public static class Body {
        private int pageNo;
        private int totalCount;
        private int numOfRows;
        private List<Item> items;

        @Getter
        @AllArgsConstructor
        public static class Item {
            private String entpName;
            private String itemName;
            private String efcyQesitm;
            private String useMethodQesitm;
            private String atpnQesitm;
            private String depositMethodQesitm;
        }
    }
}
```
## ApiService
```
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
```
1. 공공데이터포털 API에 HTTP GET 요청을 보냄
2. 응답 받은 JSON 데이터를 파싱해서 ApiResponse로 매핑
3. ApiEntity 리스트로 변환
4. MongoDB에 비동기로 저장
5. Mono<Void>로 결과 리턴 (결과 없음)

## ApiController
```
@RequiredArgsConstructor
@RestController
public class ApiController {

    private final ApiService apiService;

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

    @DeleteMapping("/drug-info")
    public void DeleteAllDrugs() {
        apiService.DeleteAllDrugs();
    }
}
```
Mono는 Spring WebFlux에서 "0개 또는 1개의 값"을 비동기로 감싸는 객체다.
외부 API로부터 데이터를 받아올 때까지 기다렸다가 응답이 오면 그 값을 Mono<String>으로 감싸서 반환한다.

Flux는 여러 개의 데이터를 비동기로 처리할 수 있는 WebFlux의 스트림이다.

.subscribe()를 통해 API 호출 및 DB 저장은 백그라운드에서 별도로 처리함.

장점: 클라이언트 타임아웃 문제 없이 대용량 작업 가능.

## ApiRepository
```
// MongoDB에 비동기로 데이터를 저장하거나 조회하는 기능을 자동으로 제공하는 인터페이스
@Repository
public interface ApiRepository extends ReactiveMongoRepository<ApiEntity, String> {
}
```
