package com.jinwoo.dev.sns.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

public class JwtTokenUtils {

    // 사용자명 추출
    public static String getUserName(String token, String key){
         return extractClaims(token, key).get("userName", String.class);
    }

    // 토큰 만료 검증
    public static boolean isExpired(String token, String key){
        Date date = extractClaims(token, key).getExpiration(); // 만료시간
        return date.before(new Date());
    }

    // Claims 추출
    private static Claims extractClaims(String token, String key){
        return Jwts.parserBuilder().setSigningKey(getKey(key))
                .build().parseClaimsJws(token).getBody();
    }

    // token 생성
    public static String generateToken(String userName, String key, long expiredTimeMs){
        Claims claims = Jwts.claims();
        claims.put("userName", userName);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiredTimeMs))
                .signWith(getKey(key), SignatureAlgorithm.HS256)
                .compact();
    }

    // 키 생성
    private static Key getKey(String key){
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
