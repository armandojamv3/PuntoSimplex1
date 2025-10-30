package com.restaurante.PuntoSimplex.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Data // lombok
@Table(name = "rol")
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rol_id")
    private Long  rol_id;

    @NotBlank(message = "La descripción del rol es obligatoria")
    @Size(max = 200,message = "La descripción no puede tener más de 200 caracteres")
    @Column( name = "descripcion",nullable = false, unique = true)
    private String descripcion;

    @Column(nullable = false)// si esta vacia o no
    private Boolean activo = true;
}
