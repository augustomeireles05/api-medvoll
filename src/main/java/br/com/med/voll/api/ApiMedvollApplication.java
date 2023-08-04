package br.com.med.voll.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
public class ApiMedvollApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiMedvollApplication.class, args);
	}

}
