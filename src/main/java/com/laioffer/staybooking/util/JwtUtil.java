package com.laioffer.staybooking.util;
// created @ 09/25 lecture 28
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;

import io.jsonwebtoken.Claims;

// 帮助生成、解析 Token 的class

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    public String generateToken(String subject) {
        return Jwts.builder()
                .setClaims(new HashMap<>())
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(SignatureAlgorithm.HS256, secret) //选择加密算法
                .compact();
    }

    private Claims extractClaims(String token) {
        //获取整个对象的body，然后用这个object读取信息， 看下面2个方法
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public Date extractExpiration(String token) {
//        extractClaims(token ).getIssuedAt()
        return extractClaims(token).getExpiration();
    }
// 验证 Expiration Date 和当前 Data的比较， Exp 在后的话返回true
    public Boolean validateToken(String token) {
        //为什么没有system.currentTimeMillis() ? 不写的话是默认 当前时间
        return extractExpiration(token).after(new Date());
    }


}