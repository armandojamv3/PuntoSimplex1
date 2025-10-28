package com.restaurante.PuntoSimplex.repository;



import com.restaurante.PuntoSimplex.Model.Mesa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MesaPedidoRepository extends JpaRepository<Mesa, Long> {
}
