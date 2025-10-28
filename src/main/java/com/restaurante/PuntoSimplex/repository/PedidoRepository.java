package com.restaurante.PuntoSimplex.repository;

import com.restaurante.PuntoSimplex.Model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
}
