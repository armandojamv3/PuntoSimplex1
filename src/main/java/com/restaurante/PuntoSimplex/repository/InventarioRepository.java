package com.restaurante.PuntoSimplex.repository;



import com.restaurante.PuntoSimplex.Model.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventarioRepository extends JpaRepository<Inventario, Long> {
}

