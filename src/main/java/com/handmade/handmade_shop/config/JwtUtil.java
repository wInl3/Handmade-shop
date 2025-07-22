package com.handmade.handmade_shop.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;
import java.security.Key;

/**
 * Lớp tiện ích dùng để xử lý JWT: tạo, xác thực, trích xuất thông tin từ token.
 */
@Component
public class JwtUtil {

    // 🔐 Khóa bí mật để ký JWT (nên cấu hình trong application.properties về sau)
    @Value("${jwt.secret}")
    private String secretKey;

    // ⏰ Thời gian sống của token (10 giờ)
    @Value("${jwt.expiration}")
    private long expiration;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Tạo JWT từ thông tin người dùng.
     *
     * @param userDetails Thông tin người dùng (username)
     * @return chuỗi JWT hợp lệ
     */
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())                          // Gắn username
                .setIssuedAt(new Date())                                       // Ngày tạo
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // Hạn token
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // Ký bằng HS256
                .compact();
    }

    /**
     * Xác thực token có hợp lệ không (đúng user và chưa hết hạn).
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    /**
     * Lấy username (subject) từ token.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Kiểm tra token đã hết hạn chưa.
     */
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Lấy thời điểm hết hạn từ token.
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Hàm tổng quát để trích xuất thông tin từ claims trong token.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);
    }
}
