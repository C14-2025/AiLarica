package br.inatel.ailarica.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Classe utilitária para criptografia e validação de senhas usando BCrypt.
 * Fornece métodos para codificar senhas e validar senhas contra hashes.
 */
@Component
public class PasswordEncoder {

    private final BCryptPasswordEncoder encoder;

    public PasswordEncoder() {
        // Strength 12 fornece um bom equilíbrio entre segurança e performance
        this.encoder = new BCryptPasswordEncoder(12);
    }

    /**
     * Codifica uma senha em texto plano para um hash BCrypt.
     *
     * @param rawPassword Senha em texto plano
     * @return Hash da senha
     */
    public String encode(String rawPassword) {
        if (rawPassword == null || rawPassword.isEmpty()) {
            throw new IllegalArgumentException("Senha não pode ser nula ou vazia");
        }
        return encoder.encode(rawPassword);
    }

    /**
     * Valida uma senha em texto plano contra um hash BCrypt.
     *
     * @param rawPassword Senha em texto plano
     * @param encodedPassword Hash da senha armazenado no banco de dados
     * @return true se a senha corresponde ao hash, false caso contrário
     */
    public boolean matches(String rawPassword, String encodedPassword) {
        if (rawPassword == null || encodedPassword == null) {
            return false;
        }
        return encoder.matches(rawPassword, encodedPassword);
    }
}
