package com.recipe.recipe_project.Jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.xml.bind.DatatypeConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenProvider {

  @Value("${jwt.secretKey}")
  private String secretkey;

  private static long expireTimeMs = 1000 * 10;

  public String createToken(String account, String pw){
    JwtBuilder builder = Jwts.builder()
        .setHeader(createHeader())
        .setClaims(createClaims(account, pw))
        .signWith(SignatureAlgorithm.HS256, createSignature())
        .setExpiration(createExpiredDate());
    return builder.compact();
  }

  private Map<String, Object> createHeader(){
    Map<String, Object> header = new HashMap<>();
    header.put("typ", "JWT");
    header.put("alg", "HS256");
    header.put("regDate", System.currentTimeMillis());

    return header;
  }

  private Map<String, Object> createClaims(String account, String pw){
    Map<String,Object> claims = new HashMap<>();
    claims.put("account", account);
    claims.put("pw", pw);

    return claims;
  }

  public Key createSignature(){
    byte[] aniKeySecretBytes = DatatypeConverter.parseBase64Binary(secretkey);
    return new SecretKeySpec(aniKeySecretBytes, SignatureAlgorithm.HS256.getJcaName());
  }

  private Date createExpiredDate(){
    Calendar c = Calendar.getInstance();
    c.add(Calendar.HOUR, 8);
    return c.getTime();
  }

  public String getUserAccount(String token) { return extractClaims(token, secretkey).get("account").toString();}
  public String getUserPw(String token) { return extractClaims(token, secretkey).get("pw").toString();}

  public boolean isExpired(String token){
    Date expiredDate = extractClaims(token, secretkey).getExpiration();
    return expiredDate.before(new Date());
  }
  public static Claims extractClaims(String token, String secretkey){
    return Jwts.parser().setSigningKey(secretkey).parseClaimsJws(token).getBody();
  }
}