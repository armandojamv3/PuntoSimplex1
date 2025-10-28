package com.restaurante.PuntoSimplex.repository;

import com.restaurante.PuntoSimplex.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
// Hereda todos los métodos CRUD de JPA
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
