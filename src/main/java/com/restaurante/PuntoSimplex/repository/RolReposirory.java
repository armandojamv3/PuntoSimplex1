package com.restaurante.PuntoSimplex.repository;

import com.restaurante.PuntoSimplex.Model.Rol;
import com.restaurante.PuntoSimplex.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolReposirory extends JpaRepository<Rol, Long> {
}
