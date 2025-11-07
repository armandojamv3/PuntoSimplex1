package com.restaurante.PuntoSimplex.repository;



import com.restaurante.PuntoSimplex.Model.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventarioRepository extends JpaRepository<Inventario, Long> {

    // Método que ProductoService necesita para obtener el stock por ID de producto
    Optional<Inventario> findByProductoId(Long productoId);
}

