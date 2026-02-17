package io.strongfoundation.joinfaces;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.context.SecurityContextHolder;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class SpringJoinfacesSessionApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringJoinfacesSessionApplication.class, args);
	}

	@PostConstruct
	public void setupSecurityContext() {
		// Esto permite que los hilos hijos hereden el contexto del padre.
		// Ayuda a que JSF vea lo que el Filtro de Spring configuró.
		SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
	}

}
