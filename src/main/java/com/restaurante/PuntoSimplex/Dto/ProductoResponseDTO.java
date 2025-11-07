package com.restaurante.PuntoSimplex.Dto;

import lombok.Builder;
import lombok.Value;


import java.math.BigDecimal;

@Value // Reemplaza @Getter, @Setter y lo hace inmutable
@Builder
public class ProductoResponseDTO {
    private Long id;

    private String nombre;
    private BigDecimal precioVenta;
    private Integer stock;
    private Long categoriaId;

    // NUEVO: Agrega el estado activo
    private boolean activo;
}
