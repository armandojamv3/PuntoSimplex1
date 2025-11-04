package com.restaurante.PuntoSimplex.repository;

import com.restaurante.PuntoSimplex.Model.Rol;
import com.restaurante.PuntoSimplex.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolReposirory extends JpaRepository<Rol, Long> {

    /**
     * Busca un rol por su descripción.
     * Es CRÍTICO para la lógica de inicialización cuando el ID es autogenerado.
     */
    Rol findByDescripcion(String descripcion);
}
