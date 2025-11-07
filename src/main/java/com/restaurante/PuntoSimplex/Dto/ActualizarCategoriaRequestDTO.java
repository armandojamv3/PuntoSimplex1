package com.restaurante.PuntoSimplex.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ActualizarCategoriaRequestDTO {

    @NotBlank(message = "La descripción de la categoría es obligatoria.")
    @Size(min = 3, max = 50, message = "La descripción debe tener entre 3 y 50 caracteres.")
    private String descripcion;

    @NotNull(message = "El estado (activo/inactivo) es obligatorio.")
    private Boolean activo;
}