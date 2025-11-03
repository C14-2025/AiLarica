package br.inatel.ailarica;

import br.inatel.ailarica.LoginRequest;
import br.inatel.ailarica.LoginController;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    private final LoginService loginService;
    private static final String SESSION_ATTR = "authenticatedUser";

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping("/login")
    public String showLogin(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(LoginRequest loginRequest, Model model, HttpSession session) {
        boolean ok = loginService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());
        if (ok) {
            session.setAttribute(SESSION_ATTR, loginRequest.getUsername());
            return "redirect:/home";
        } else {
            model.addAttribute("error", "Credenciais inválidas");
            return "login";
        }
    }

    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        Object user = session.getAttribute(SESSION_ATTR);
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "home";
    }
}
