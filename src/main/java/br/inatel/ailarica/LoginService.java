package br.inatel.ailarica;

import org.springframework.stereotype.Service;

@Service
public class LoginService {

    // Credenciais mockadas
    private static final String MOCK_USER = "user";
    private static final String MOCK_PASS = "password";

    public boolean authenticate(String username, String password) {
        if (username == null || password == null) {
            return false;
        }
        return MOCK_USER.equals(username) && MOCK_PASS.equals(password);
    }
}
