# JWT으로 로그인 기능 구현하기
[코드](https://github.com/skcy1515/Repository/tree/main/%EA%B8%B0%EB%8A%A5%20%EA%B5%AC%ED%98%84%20%EC%97%B0%EC%8A%B5/%EB%A1%9C%EA%B7%B8%EC%9D%B8%20%EA%B5%AC%ED%98%84/demo2/src/main/java/com/example/demo2)
## 사전 설정
### 필수 의존성
```
implementation 'org.springframework.boot:spring-boot-starter-security'
implementation 'io.jsonwebtoken:jjwt-api:0.11.5'
implementation 'io.jsonwebtoken:jjwt-impl:0.11.5'
implementation 'io.jsonwebtoken:jjwt-jackson:0.11.5'
```

### application.properties
```
jwt.secret=YOUR_SECRET_KEY
jwt.access-token-expiration=60000 (밀리초)
jwt.refresh-token-expiration=120000 (밀리초)
```

# 기능
![1](https://github.com/user-attachments/assets/57453243-8d3a-4922-8410-3cf842ddf95a)

![2](https://github.com/user-attachments/assets/31f79051-f9c0-430f-bc71-a5779e8da5b3)

![4](https://github.com/user-attachments/assets/964a8120-e2e5-4bc3-9908-cedeebc4dbf3)

로그인을 하면 토큰을 받고, 인증이 필요한 페이지에서 엑세스 토큰으로 인증 요청을 함. 만약 엑세스 토큰이 없으면
쿠키에 저장된 리프레시 토큰을 이용해 엑세스 토큰을 다시 발급받고, 리프레시 토큰마저 없거나 만료되면
로그인 페이지로 돌아감. 로그아웃 버튼을 누르면 로컬스토리지에 있는 엑세스 토큰과 쿠키에 있는
리프레시 토큰이 삭제됨

# 코드 설명
## JwtTokenProvider
```
@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.access-token-expiration}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    private Key key;

    public JwtTokenProvider(@Value("${jwt.secret}") String jwtSecret) {
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public String generateAccessToken(String email) {
        return generateToken(email, accessTokenExpiration);
    }

    public String generateRefreshToken(String email) {
        return generateToken(email, refreshTokenExpiration);
    }

    private String generateToken(String email, long expiration) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setSubject(email) // 이메일을 토큰의 서브젝트로 설정
                .setIssuedAt(now) // 토큰이 발급된 시간
                .setExpiration(expiryDate) // 토큰 만료 시간
                .signWith(key, SignatureAlgorithm.HS512) // 토큰 서명
                .compact();
    }

    // 토큰에서 이메일 (서브젝트 반환)
    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public Long getRefreshTokenExpiration() {
        return refreshTokenExpiration;
    }
}
```
토큰을 발급, 조회, 검증하는 코드

## JwtAuthenticationFilter
```
@Component
@RequiredArgsConstructor
// JWT 토큰을 검사하고 유효한 경우 인증 정보를 SecurityContext에 설정하는 Spring Security 필터
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider; // JWT 토큰을 생성하고 검증
    private final UserDetailService userDetailService;

    @Override
    // 모든 요청을 처리하는 메서드. 요청이 들어올 때마다 실행
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = getJwtFromRequest(request); // 요청 헤더에서 토큰을 가져오는 메서드

        if (token != null && jwtTokenProvider.validateToken(token)) {
            String email = jwtTokenProvider.getEmailFromToken(token);

            UserDetails userDetails = userDetailService.loadUserByUsername(email); // 이메일로부터 사용자 정보 가져옴
            // 인증 객체 생성 (사용자 정보, 비밀번호 (따로 전달하지 않음), 사용자 권한)
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            // 인증 객체에 요청의 세부 정보를 설정
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            // Spring Security의 보안 컨텍스트에 인증 객체 설정
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        // 요청과 응답을 필터 체인의 다음 필터로 넘김
        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization"); // Authorization 헤더에서 JWT 토큰을 추출
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) { // Bearer 라는 접두사를 제거하고 나머지 토큰을 반환
            return bearerToken.substring(7);
        }
        return null;
    }
}
```
Spring Security의 커스텀 필터로, 클라이언트로부터 받은 요청의 Authorization 헤더에 포함된 JWT 토큰을 검증하고, 인증 정보(Authentication)를 SecurityContext에 설정하는 역할을 한다

## WebSecurityConfig
```
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter; // JWT 기반의 인증을 처리하는 커스텀 필터

    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring()
                .requestMatchers(new AntPathRequestMatcher("/static/**"));
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/signUp", "/refreshToken").permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/api/**")).authenticated()
                        .anyRequest().permitAll())
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) // JWT 필터 등록
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder, UserDetailService userDetailService) throws Exception {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailService);
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder);
        return new ProviderManager(daoAuthenticationProvider);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```
기존 WebSecurityConfig에서 JWT 필터 등록

## AuthService
```
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    // 로그인 요청만 수행
    public AuthResponse authenticateUser(String email, String password) {
        // 전달된 이메일과 비밀번호로 인증 수행
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password));

        // Spring Security의 SecurityContext 에 인증 정보를 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtTokenProvider.generateAccessToken(email);
        String refreshToken = jwtTokenProvider.generateRefreshToken(email);

        return new AuthResponse(accessToken, refreshToken);
    }

    // 회원가입 메서드
    public void signUp(String email, String password) {
        Optional<UserEntity> existingEmail = userRepository.findByEmail(email);
        if (existingEmail.isPresent()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "이미 존재하는 이메일입니다.");
        }

        UserEntity userEntity = UserEntity.builder().
                password(bCryptPasswordEncoder.encode(password)).
                email(email).
                build();
        userRepository.save(userEntity);
    }
}
```
사용자 인증과 회원가입을 처리

## AuthController
```
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider; // JWT 토큰을 생성하고 검증하는 유틸리티 클래스
    private final UserDetailService userDetailService; // 사용자 정보를 불러오는 서비스
    private final AuthService authService; // 사용자 인증을 처리하는 서비스

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody UserRequest loginRequest) {
        AuthResponse authResponse = authService.authenticateUser(loginRequest.getEmail(), loginRequest.getPassword());

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", authResponse.getRefreshToken())
                .httpOnly(true) // JavaScript로 접근할 수 없도록 설정
                // HTTPS에서만 쿠키가 전송되도록 설정
                // .secure(true)
                .sameSite("Strict") // CSRF 공격 방지
                .path("/") // 쿠키의 유효 경로
                .maxAge(jwtTokenProvider.getRefreshTokenExpiration() / 1000) // 쿠키의 만료 시간 설정 (초 단위)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(new AccessTokenResponse(authResponse.getAccessToken()));
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(@CookieValue(value = "refreshToken", required = false) String refreshToken) {
        if (jwtTokenProvider.validateToken(refreshToken)) { // 전달받은 리프레시 토큰이 유효한지 검증
            String email = jwtTokenProvider.getEmailFromToken(refreshToken); // 리프레시 토큰으로부터 이메일을 추출

            String newAccessToken = jwtTokenProvider.generateAccessToken(email); // 액세스 토큰 생성
            return ResponseEntity.ok(new AccessTokenResponse(newAccessToken));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("리프레시 토큰이 만료되었습니다.");
        }
    }

    // 회원가입 요청
    @PostMapping("/signUp")
    public ResponseEntity<String> signUp(@RequestBody UserRequest userRequest) {
        try {
            authService.signUp(userRequest.getEmail(), userRequest.getPassword());
            return ResponseEntity.ok("회원가입이 완료되었습니다.");
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }

    // 로그아웃
    @PostMapping("/api/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {
        // 쿠키 삭제: 'refreshToken' 쿠키를 만료시킨다.
        Cookie cookie = new Cookie("refreshToken", null);  // 값은 null로 설정
        cookie.setPath("/");  // 쿠키 경로 설정 (로그인 시 사용한 경로와 동일해야 함)
        cookie.setMaxAge(0);  // 쿠키 만료 시간 설정 (0으로 설정하면 삭제됨)

        // 쿠키를 클라이언트에 전송하여 삭제
        response.addCookie(cookie);

        return ResponseEntity.ok("로그아웃 되었습니다.");
    }

    // 인증 요청
    @GetMapping("/api/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("인증 성공.");
    }
}
```
사용자 인증 및 토큰 관리를 처리하는 RestController로, 로그인, 로그아웃, 토큰 갱신, 회원가입, 인증 테스트 등의 기능을 제공

## JS 토큰 인증 코드 예시
```
    // 로그인 요청
    $.ajax({
	url: '/login',  // 서버의 로그인 API URL
	method: 'POST',
	contentType: 'application/json',
	data: JSON.stringify({
	    email: email,
	    password: password
	}),
	success: function(response) {
	    localStorage.setItem('accessToken', response.accessToken);  // 액세스 토큰 저장
	    window.location.href = '/';  // 로그인 후 대시보드로 이동
	},
	error: function(xhr, status, error) {
	    // 로그인 실패 시, 오류 메시지를 표시합니다.
	    $('#error-message').show();
	}
```
로그인 요청을 보내면 Controller의 로그인 함수와 매핑, 이메일과 비밀번호를 인증하고 액세스 토큰과 리프레시 토큰을 발급, 
리프레시 토큰은 쿠키에 저장하고 엑세스 토큰은 로컬 스토리지에 저장

```
    $(document).ready(function() {
        var accessToken = localStorage.getItem('accessToken');
        loadProtectedContent(accessToken)

        // 로그아웃 버튼 클릭 시 로컬 스토리지 초기화 및 로그인 페이지로 이동
        $('#logoutButton').click(function() {
            // 서버에 로그아웃 요청
            $.ajax({
                url: '/api/logout',  // 서버의 로그아웃 API 엔드포인트
                headers: { 'Authorization': 'Bearer ' + accessToken },
                method: 'POST',
                success: function(response) {
                    alert(response);
                    localStorage.removeItem('accessToken');
                    window.location.href = '/login';
                },
                error: function() {
                    window.location.href = '/login';
                }
            });
        });
    });

    function loadProtectedContent(accessToken) {
        $.ajax({
            url: '/api/test',
            method: 'GET',
            headers: { 'Authorization': 'Bearer ' + accessToken },
            success: function(response) {
                $('#content').text(response);
            },
            error: function(xhr) {
                if (xhr.status === 403) { // 403 오류일 경우
                    console.log("엑세스 토큰 만료로 다시 받습니다.")
                    refreshAccessToken(); // 리프레시 토큰으로 액세스 토큰 갱신
                } else {
                    alert("콘텐츠를 불러오지 못했습니다.");
                }
            }
        });
    }

    function refreshAccessToken() {
        $.ajax({
            url: '/refreshToken', // 리프레시 토큰으로 액세스 토큰 갱신 요청
            method: 'POST',
            contentType: 'application/json',
            success: function(response) {
                // 새로 발급받은 액세스 토큰 저장
                localStorage.setItem('accessToken', response.accessToken);
                loadProtectedContent(response.accessToken); // 다시 콘텐츠 요청
            },
            error: function() {
                alert("리프레시 토큰 만료");
                redirectToLogin();
            }
        });
    }

    function redirectToLogin() {
        alert("로그인이 필요합니다.");
        window.location.href = '/login';
    }
```
- 로그아웃: 사용자가 로그아웃 버튼을 누르면 리프레시 토큰 쿠키를 삭제하고 엑세스 토큰을 로컬 스토리지에서 제거
- 인증 요청: /api/test 엔드포인트로 보호된 리소스를 요청, 요청 헤더에 Authorization: Bearer <accessToken> 형태로 액세스 토큰을 포함
	- 실패: HTTP 상태 코드 403이면 엑세스 토큰이 만료된 것으로 판단하고 refreshAccessToken() 함수를 호출하여 갱신을 시도
- 엑세스 토큰 갱신: 서버로 /refreshToken 요청을 보내 새로 발급된 액세스 토큰을 받아옴, 서버 측에서 @CookieValue 어노테이션을 사용하여 refreshToken 쿠키를 자동으로 추출
	- 실패: 로그인 페이지로 리다이렉트
