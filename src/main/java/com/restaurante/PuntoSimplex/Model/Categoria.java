package com.restaurante.PuntoSimplex.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Optional;

@Entity // 🛑 ¡CRÍTICO! Marca la clase como una tabla.
@Table(name = "categoria")
@Data
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String descripcion;


    // ✨ CÓDIGO CORREGIDO: Usamos la definición DDL correcta para PostgreSQL
    @Column(nullable = false)
    private Boolean activo = true;

    // 4. MANTÉN el @PrePersist como buena práctica.
    @PrePersist
    public void prePersist() {
        if (activo == null) {
            activo = true;
        }
    }
}
