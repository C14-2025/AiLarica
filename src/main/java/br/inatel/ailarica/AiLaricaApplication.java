package br.inatel.ailarica;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AiLaricaApplication {
    public static void main(String[] args) {
        SpringApplication.run(AiLaricaApplication.class, args);
        System.out.println("🚀 Servidor iniciado em http://localhost:8080/");
    }
}
