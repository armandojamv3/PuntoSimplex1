package com.restaurante.PuntoSimplex.Dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ActualizarProductoRequestDTO {

    @NotBlank(message = "El nombre no puede estar vacío.")
    private String nombre;

    @NotNull(message = "El precio de venta es obligatorio.")
    @DecimalMin(value = "0.01", message = "El precio de venta debe ser mayor a cero.")
    private Double precioVenta;

    // Asumo que tienes precioCompra en tu Modelo Producto
    @NotNull(message = "El precio de compra es obligatorio.")
    @DecimalMin(value = "0.01", message = "El precio de compra debe ser mayor a cero.")
    private Double precioCompra;

    @NotNull(message = "El ID de la categoría es obligatorio.")
    @Min(value = 1, message = "El ID de la categoría no es válido.")
    private Long categoriaId;

    // Incluir el estado 'activo' si el front-end lo permite cambiar desde la misma ventana de edición
    @NotNull(message = "El estado (activo) es obligatorio.")
    private Boolean activo;
}