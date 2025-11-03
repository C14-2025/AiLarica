package br.inatel.ailarica;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginServiceTest {

    private final LoginService loginService = new LoginService();

    @Test
    void authenticate_withValidCredentials_returnsTrue() {
        assertTrue(loginService.authenticate("user", "password"));
    }

    @Test
    void authenticate_withInvalidCredentials_returnsFalse() {
        assertFalse(loginService.authenticate("user", "wrong"));
        assertFalse(loginService.authenticate(null, "password"));
        assertFalse(loginService.authenticate("user", null));
    }
}
