package com.admin.panel.service;


import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import com.admin.panel.configuration.data.AuthenticationConfiguration;
import com.admin.panel.domain.entity.BlacklistedToken;
import com.admin.panel.domain.entity.UserEntity;
import com.admin.panel.domain.repository.TokenRepository;
import com.admin.panel.exceptions.InvalidTokenException;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    private final AuthenticationConfiguration authenticationConfiguration;
    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);
    private final TokenRepository tokenRepository;

    public JwtService(AuthenticationConfiguration authenticationConfiguration, TokenRepository tokenRepository) {
        this.authenticationConfiguration = authenticationConfiguration;
        this.tokenRepository = tokenRepository;
    }


    // TOKEN EXTRACTION
    public Long extractUserId(String token) {
        Claims claims = extractAllClaims(token);
        String subject = claims.getSubject();

        try {
            return Long.parseLong(subject);
        } catch (NumberFormatException e) {
            throw new InvalidTokenException("User ID in token is not a valid number");
        }
    }

    public Long getLoggedInUserId(HttpServletRequest request, String tokenType) {
        String token = extractAndValidateToken(request, tokenType);
        return extractUserId(token);
    }

    public Date extractExpiration(String token) {
        Claims claims = extractAllClaims(token);
        return claims.getExpiration();
    }

    public String extractAndValidateToken(HttpServletRequest request, String expectedType) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new InvalidTokenException("Authorization header missing or invalid");
        }

        String token = authHeader.substring(7); // Extract token
        validateToken(token, expectedType, true); // Validate token against type
        return token; // Return valid token
    }



    // TOKEN VALIDATION
    public Boolean validateToken(String token, String expectedType, boolean validateTokeType) {
        if (isTokenBlacklisted(token) || isTokenExpired(token)) {
            throw new InvalidTokenException("Token is invalid or expired");
        }

        try {
            JwtParser parser = Jwts.parser()
                    .setSigningKey(getSigninKey())
                    .build();
            parser.parseClaimsJws(token);

            if (!validateTokenType(token, expectedType) && validateTokeType) {
                throw new InvalidTokenException("Token type mismatch: expected " + expectedType);
            }

        } catch (Exception e) {
            throw new InvalidTokenException("JWT validation error: " + e.getMessage());
        }
        return Boolean.TRUE;
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private boolean validateTokenType(String token, String expectedType) {
        Claims claims = extractAllClaims(token);
        String tokenType = claims.get("type", String.class);
        return expectedType.equals(tokenType);
    }

    private boolean isTokenBlacklisted(String token) {
        return tokenRepository.findByToken(token).isPresent();
    }









    // TOKEN GENERATION
    public String generateAccessToken(UserEntity user) {
        return generateToken(user, authenticationConfiguration.getJwtExpiration(), "accessToken");
    }

    public String generateRefreshToken(UserEntity user) {
        return generateToken(user, authenticationConfiguration.getJwtRefreshExpiration(), "refreshToken");
    }






    // TOKEN BLACKLISTING
    public void blacklistToken(String token, Date expiryDate) {
        BlacklistedToken blacklistedToken = new BlacklistedToken();
        blacklistedToken.setToken(token);
        blacklistedToken.setExpiryDate(expiryDate);
        tokenRepository.save(blacklistedToken);
    }

    public void cleanUpExpiredTokens() {
        tokenRepository.deleteByExpiryDateBefore(new Date());
    }

    // UTILITY TOKEN METHODS
    private String generateToken(UserEntity user, Long expireTime, String tokenType) {
        return Jwts.builder()
                .subject(user.getId().toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expireTime))
                .claim("type", tokenType)
                .signWith(getSigninKey())
                .compact();
    }
    //     GET INFORMATION STORED IN THE TOKEN
    //     userID
    //     expirationDate
    //     tokenType - ( accessToken OR refreshToken )
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getSigninKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private SecretKey getSigninKey() {
        byte[] keyBytes = Decoders.BASE64.decode(authenticationConfiguration.getJwtSecret());
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
