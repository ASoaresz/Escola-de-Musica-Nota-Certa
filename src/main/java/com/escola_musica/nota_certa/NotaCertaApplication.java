package com.escola_musica.nota_certa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@org.springframework.boot.persistence.autoconfigure.EntityScan(basePackages = "com.escola_musica.nota_certa.entity")
public class NotaCertaApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotaCertaApplication.class, args);
	}

}
