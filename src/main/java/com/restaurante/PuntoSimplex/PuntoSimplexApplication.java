package com.restaurante.PuntoSimplex;

import com.restaurante.PuntoSimplex.Model.Rol;
import com.restaurante.PuntoSimplex.Model.Usuario;
import com.restaurante.PuntoSimplex.repository.RolReposirory;
import com.restaurante.PuntoSimplex.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@SpringBootApplication



public class PuntoSimplexApplication {

	public static void main(String[] args) {
		SpringApplication.run(PuntoSimplexApplication.class, args);
	}

}