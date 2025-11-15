package com.bruteforce.userasaservice.security;

import com.bruteforce.userasaservice.model.Role;
import com.bruteforce.userasaservice.model.User;
import com.bruteforce.userasaservice.model.VerificationStatus;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpirationInMs;


    /**
     * This method generates a JWT token with only the username and signature.
     * It is more secure because it does not include any extra claims.
     *
     * @param user the user object
     * @return the generated JWT token
     */
    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole().name());  // Add role claim
        claims.put("email", user.getEmail());       // Add other useful claims
        claims.put("userId", user.getUserId());     // Add user ID

        // For lawyers, you might want to add additional claims
        if (user.getRole() == Role.LAWYER && user.getLawyerProfile() != null) {
            claims.put("lawyerId", user.getLawyerProfile().getLawyerId());
            claims.put("isVerified", user.getLawyerProfile().getVerificationStatus() == VerificationStatus.VERIFIED);
        }

        return Jwts.builder()
                .claims(claims)
                .subject(user.getUserName())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                .signWith(getSignInKey())
                .compact();
    }
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}