package com.restaurante.PuntoSimplex.repository;

import com.restaurante.PuntoSimplex.Model.Categoria;
import com.restaurante.PuntoSimplex.Model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    // Método nuevo para verificar si una descripción ya existe
    Optional<Categoria> findByDescripcion(String descripcion);

}
