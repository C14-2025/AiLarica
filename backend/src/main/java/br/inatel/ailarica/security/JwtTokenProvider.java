package br.inatel.ailarica.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * Classe utilitária para geração e validação de tokens JWT.
 * Fornece métodos para criar tokens, extrair informações e validar tokens.
 */
@Component
public class JwtTokenProvider {

    @Value("${jwt.secret:AiLarica_Secret_Key_2025_Very_Secure_Key_For_JWT_Tokens}")
    private String jwtSecret;

    @Value("${jwt.expiration:86400000}")  // 24 horas em milissegundos
    private long jwtExpirationMs;

    /**
     * Gera um token JWT para um usuário/restaurante.
     *
     * @param id ID do usuário/restaurante
     * @param email Email do usuário/restaurante
     * @param tipo Tipo de usuário (USUARIO ou RESTAURANTE)
     * @return Token JWT gerado
     */
    public String generateToken(int id, String email, String tipo) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .subject(email)
                .claim("id", id)
                .claim("tipo", tipo)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(key)
                .compact();
    }

    /**
     * Extrai o email do token JWT.
     *
     * @param token Token JWT
     * @return Email contido no token
     */
    public String getEmailFromToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }

    /**
     * Extrai o ID do token JWT.
     *
     * @param token Token JWT
     * @return ID contido no token
     */
    public Integer getIdFromToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return (Integer) claims.get("id");
    }

    /**
     * Extrai o tipo de usuário do token JWT.
     *
     * @param token Token JWT
     * @return Tipo de usuário (USUARIO ou RESTAURANTE)
     */
    public String getTipoFromToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return (String) claims.get("tipo");
    }

    /**
     * Valida um token JWT.
     *
     * @param token Token JWT a ser validado
     * @return true se o token é válido, false caso contrário
     */
    public boolean validateToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Extrai o token JWT do header Authorization.
     * Espera o formato: "Bearer <token>"
     *
     * @param authHeader Header Authorization
     * @return Token JWT ou null se o formato for inválido
     */
    public String extractTokenFromHeader(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
}
