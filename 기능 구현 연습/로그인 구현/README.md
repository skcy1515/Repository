# JWT으로 로그인 기능 구현하기
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
