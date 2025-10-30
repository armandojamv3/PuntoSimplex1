package com.restaurante.PuntoSimplex.repository;

import com.restaurante.PuntoSimplex.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
// Hereda todos los métodos CRUD de JPA
//es la capa que habla directamente con la base de datos.
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Buscar usuario por nombre de usuario
    Optional<Usuario> findByUsuario(String usuario);
}
