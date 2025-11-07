package com.restaurante.PuntoSimplex.Dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CrearProductoRequestDTO {



    // 2. Nombre del producto
    // 2.2 Obligatorio: @NotBlank
    // 2.4 Longitud: min 3, max 20
    // 2.3 No permite números (solo texto y espacios): @Pattern
    @NotBlank(message = "El nombre del producto es obligatorio.")
    @Size(min = 3, max = 20, message = "El nombre debe tener entre 3 y 20 caracteres.")
    // Permite letras, tildes, ñ/Ñ y espacios
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$", message = "El nombre solo debe contener letras.")
    private String nombre;

    // 3. Precio de Venta
    // 3.1 Obligatorio: @NotNull
    // 3.2 Solo acepta valores numéricos positivos. Min 3.00: @DecimalMin
    // 3.3 Máximo 10 dígitos (parte entera)
    @NotNull(message = "El precio de venta es obligatorio.")
    @DecimalMin(value = "3.00", inclusive = true, message = "El precio mínimo es 3.00.")
    @Digits(integer = 10, fraction = 2, message = "El precio excede el formato permitido (máx 10 dígitos enteros y 2 decimales).")
    private BigDecimal precioVenta;

    // 4. Categoría ID
    // 4.1 Obligatorio. Usamos el ID de la Categoría
    @NotNull(message = "La categoría es obligatoria.")
    @Min(value = 1, message = "El ID de categoría no es válido.")
    private Long categoriaId;

    // 5. Cantidad Inicial
    // 5.1 Campo obligatorio.
    @NotNull(message = "La cantidad inicial es obligatoria.")
    @Min(value = 1, message = "La cantidad inicial debe ser al menos 1.")
    private Integer stockInicial;
}
