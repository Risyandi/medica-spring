package com.api.employee.util;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.InvalidKeyException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private int jwtExpiration;

    /**
     * Creates a secure signing key from the application secret.
     * Ensures the key is compatible with HS512 (minimum 32 bytes).
     * @return The SecretKey used for signing/verifying JWTs.
     */
    private SecretKey getSigningKey() {
        // Use standard UTF-8 encoding for the secret string
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Generates a JWT token for the specified user email and profile.
     * @param email The principal (subject) of the token.
     * @param profile The user role/profile to be included in claims.
     * @return The signed JWT token string.
     */
    public String generateToken(String email, String profile) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        return Jwts.builder()
                .subject(email)
                .claims(Map.of("profile", profile))
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey(), Jwts.SIG.HS512)
                .compact();
    }

    /**
     * Retrieves the user email (Subject) from a given JWT token.
     * @param token The JWT token string.
     * @return The email associated with the token.
     */
    public String getEmailFromToken(String token) {
        var claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }

    /**
     * Retrieves the user profile (Role) from a given JWT token.
     * @param token The JWT token string.
     * @return The profile/role associated with the token.
     */
    public String getProfileFromToken(String token) {
        var claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.get("profile", String.class);
    }

    /**
     * Validates the integrity and expiration of a JWT token.
     * @param token The JWT token string.
     * @return true if the token is valid, false otherwise.
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (SignatureException | InvalidKeyException | MalformedJwtException e) {
            System.err.println("Invalid JWT Signature/Format: " + e.getMessage());
        } catch (ExpiredJwtException e) {
            System.err.println("Expired JWT Token: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.err.println("Unsupported JWT: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("JWT Claims string is empty: " + e.getMessage());
        }
        return false;
    }
}