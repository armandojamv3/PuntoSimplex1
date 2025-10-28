package com.restaurante.PuntoSimplex.repository;



import com.restaurante.PuntoSimplex.Model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
}
