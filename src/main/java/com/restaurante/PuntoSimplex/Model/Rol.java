package com.restaurante.PuntoSimplex.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "rol")
@NoArgsConstructor
@AllArgsConstructor
public class Rol {

    @Id
    // Quitamos @GeneratedValue para que uses IDs manuales
    @Column(name = "rol_id")
    private Long rol_id;

    @NotBlank(message = "La descripción del rol es obligatoria")
    @Size(max = 200, message = "La descripción no puede tener más de 200 caracteres")
    @Column(name = "descripcion", nullable = false, unique = true)
    private String descripcion;

    @Column(nullable = false)
    private Boolean activo = true;

    // Constructor útil para crear roles rápidos con ID manual
    public Rol(Long rol_id, String descripcion) {
        this.rol_id = rol_id; // Asegúrate de asignar el ID
        this.descripcion = descripcion;
        this.activo = true;
    }
}
