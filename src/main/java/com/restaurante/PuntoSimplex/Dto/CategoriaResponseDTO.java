package com.restaurante.PuntoSimplex.Dto;

import lombok.Builder;
import lombok.Value;

@Value // Hace el DTO inmutable (buena práctica para respuestas)
@Builder
public class CategoriaResponseDTO {
    private Long id;
    private String descripcion;

    // Este campo puede ser útil para el front-end
    private Boolean activo;
}