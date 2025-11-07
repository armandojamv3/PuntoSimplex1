package com.restaurante.PuntoSimplex.Dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO utilizado para registrar la entrada de stock (aumento de inventario) de un producto.
 */
@Data
public class RegistrarEntradaStockRequestDTO {
    @NotNull(message = "El ID del producto es obligatorio.")
    private Long productoId;

    @NotNull(message = "La cantidad es obligatoria.")
    @Min(value = 1, message = "La cantidad debe ser al menos 1.")
    private Integer cantidad;

    // Aquí se podrían agregar campos adicionales en el futuro (ej: fecha, proveedor, costo de compra)
}
