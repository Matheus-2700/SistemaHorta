package com.horta;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Classe principal da aplicação Sistema de Gerenciamento de Horta
 * 
 * @author Sistema Horta
 * @version 1.0.0
 */
@SpringBootApplication
@EnableScheduling
public class SistemaHortaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SistemaHortaApplication.class, args);
        System.out.println("🌱 Sistema de Gerenciamento de Horta iniciado com sucesso!");
        System.out.println("📖 Documentação da API: http://localhost:8080/api/swagger-ui.html");
        System.out.println("🌐 Interface Web: http://localhost:8080/api/");
    }
}

