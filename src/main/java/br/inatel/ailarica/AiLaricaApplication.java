package br.inatel.ailarica;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "br.inatel.ailarica")
public class AiLaricaApplication {
    public static void main(String[] args) {
        SpringApplication.run(AiLaricaApplication.class, args);
        System.out.println("🚀 Servidor iniciado em http://localhost:8080/");
    }
}