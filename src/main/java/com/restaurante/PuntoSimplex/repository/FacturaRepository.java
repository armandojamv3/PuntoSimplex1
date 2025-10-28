package com.restaurante.PuntoSimplex.repository;



import com.restaurante.PuntoSimplex.Model.Factura;
import org.springframework.data.jpa.repository.JpaRepository;

// Hereda todos los métodos CRUD de JPA
public interface FacturaRepository extends JpaRepository<Factura, Long> {
}
