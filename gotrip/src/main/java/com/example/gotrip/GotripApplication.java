package com.example.gotrip;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GotripApplication {

	public static void main(String[] args) {
		SpringApplication.run(GotripApplication.class, args);
	}

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI().info(new Info()
						.title("GoTrip API")
						.version("1.0.0")
						.description("GoTrip es una plataforma de turismo que permite a los usuarios buscar y reservar vuelos y hoteles de manera fácil y rápida. Esta API proporciona los endpoints necesarios para la gestión de vuelos, hoteles y reservas.")
				);
	}
}
