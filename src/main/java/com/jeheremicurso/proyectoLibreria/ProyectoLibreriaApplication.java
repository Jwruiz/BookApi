package com.jeheremicurso.proyectoLibreria;

import com.jeheremicurso.proyectoLibreria.principal.Principal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProyectoLibreriaApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ProyectoLibreriaApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal();
		principal.muestraElMenu();
	}
}
