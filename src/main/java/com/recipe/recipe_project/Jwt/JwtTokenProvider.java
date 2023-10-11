package com.recipe.recipe_project.Jwt;

import com.recipe.recipe_project.Security.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.xml.bind.DatatypeConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

  @Value("${jwt.secretKey}")
  private String secretkey;
  private final RedisTemplate<String, String> redisTemplate;
  @Value("${jwt.token.access-expiration-time}")
  private long accessExpirationTime;

  @Value("${jwt.token.refresh-expiration-time}")
  private long refreshExpirationTime;
  @Autowired
  private CustomUserDetailsService customUserDetailsService;

  // Access토큰 생성
  public String createAccessToken(String account, String pw){
    Date now = new Date();
    Date expiredDate = new Date(now.getTime() + accessExpirationTime);
    JwtBuilder builder = Jwts.builder()
        .setHeader(createHeader())
        .setClaims(createClaims(account))
        .setIssuedAt(now)
        .setExpiration(expiredDate)
        .signWith(SignatureAlgorithm.HS256, secretkey);
    return builder.compact();
  }
  // Refresh토큰 생성
  public String createRefreshToken(String account, String pw){
    Date now = new Date();
    Date expiredDate = new Date(now.getTime() + refreshExpirationTime);
    String refreshToken = Jwts.builder()
        .setHeader(createHeader())
        .setClaims(createClaims(account))
        .setIssuedAt(now)
        .setExpiration(expiredDate)
        .signWith(SignatureAlgorithm.HS256, secretkey)
        .compact();
    redisTemplate.opsForValue().set(
        "account",
        refreshToken
    );
    return refreshToken;
  }
  private Map<String, Object> createHeader(){
    Map<String, Object> header = new HashMap<>();
    header.put("typ", "JWT");
    header.put("alg", "HS256");
    header.put("regDate", System.currentTimeMillis());

    return header;
  }

  private Claims createClaims(String account){
    Claims claims = Jwts.claims().setSubject(account);

    return claims;
  }
  public Authentication getAuthentication(String token){
    String account = Jwts.parser()
        .setSigningKey(secretkey)
        .parseClaimsJws(token)
        .getBody().get("sub").toString();
    UserDetails userDetails = customUserDetailsService.loadUserByUsername(account);
    return new UsernamePasswordAuthenticationToken(userDetails,"",userDetails.getAuthorities());
  }
  public String getUserAccount(String token) { return extractClaims(token, secretkey).get("sub").toString();}
  public String getUserPw(String token) { return extractClaims(token, secretkey).get("pw").toString();}

  public boolean isExpired(String token){
    Date expiredDate = extractClaims(token, secretkey).getExpiration();
    return expiredDate.before(new Date());
  }
  public static Claims extractClaims(String token, String secretkey){
    return Jwts.parser().setSigningKey(secretkey).parseClaimsJws(token).getBody();
  }
}