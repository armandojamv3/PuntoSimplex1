package com.restaurante.PuntoSimplex.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
@Table(name = "PRODUCTOS")
public class Producto{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Solo anotaciones JPA para la base de datos: unicidad y longitud
    @Column(nullable = false, unique = true, length = 20)
    private String nombre;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal precioVenta;

    @Column(name = "precio") // Mapea este campo a la columna problemática 'precio'

    private BigDecimal precioFloat;

    // Relación Many-to-One: la Entidad completa
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    //   Campo para gestionar el estado
    @Column( nullable = false, columnDefinition = "boolean default true")
    private Boolean activo = true; // El valor 'true' en Java sigue siendo importante para nuevos objetos
}
