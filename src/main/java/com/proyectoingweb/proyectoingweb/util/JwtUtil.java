package com.proyectoingweb.proyectoingweb.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    // Clave secreta para firmar los tokens (en producción debe estar en variables de entorno)
    private String SECRET_KEY = "mySecretKeyForJWTTokenGenerationAndValidationInThisSpringBootApplication2024";
    
    // Duración del token en milisegundos (24 horas)
    private long JWT_EXPIRATION = 86400000;

    // Generar clave secreta
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    // ========== GENERAR TOKEN ==========
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    // Crear token con claims personalizados
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // ========== EXTRAER INFORMACIÓN DEL TOKEN ==========
    
    // Extraer username del token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    // Extraer fecha de expiración
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    // Extraer claim específico del token
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    // Extraer todos los claims del token
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // ========== VALIDAR TOKEN ==========

    // Verificar si el token ha expirado
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    // Validar token con UserDetails
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // Validar si el token es válido (sin UserDetails)
    public Boolean isTokenValid(String token) {
        try {
            return !isTokenExpired(token);
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // ========== MÉTODOS ADICIONALES ==========

    // Obtener tiempo restante del token (en minutos)
    public Long getTimeUntilExpiration(String token) {
        Date expiration = getExpirationDateFromToken(token);
        long timeLeft = expiration.getTime() - System.currentTimeMillis();
        return timeLeft > 0 ? timeLeft / (1000 * 60) : 0; // Convertir a minutos
    }

    // Verificar si el token necesita renovación (menos de 30 minutos restantes)
    public Boolean shouldTokenBeRenewed(String token) {
        return getTimeUntilExpiration(token) < 30;
    }
}