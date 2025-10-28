package com.restaurante.PuntoSimplex.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Data
@Table(name = "PRODUCTOS")
public class Producto{

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;


private String nombre;
private double precio;
}
