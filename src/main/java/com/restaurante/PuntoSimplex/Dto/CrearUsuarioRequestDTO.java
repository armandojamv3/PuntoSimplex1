package com.restaurante.PuntoSimplex.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;

//Representar el cuerpo de la petición HTTP (el JSON que te envían).
@Data
public class CrearUsuarioRequestDTO {

    @NotBlank(message = "El primer nombre del usuario es obligatorio")
    @Size(max = 20, message = "El nombre no puede tener más de 20 caracteres")
    private String primerNombre;

    @Size(max = 20, message = "El segundo nombre no puede tener más de 20 caracteres")
    private String segundoNombre;

    @NotBlank(message = "El primer apellido es obligatorio")
    @Size(max = 20, message = "El apellido no debe tener más de 20 caracteres")
    private String primerApellido;

    @Size(max = 20, message = "El segundo apellido no debe tener más de 20 caracteres")
    private String segundoApellido;

    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(min = 4, max = 20, message = "El usuario debe tener entre 4 y 20 caracteres")
    private String usuario;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, max = 20, message = "La contraseña debe tener entre 6 y 20 caracteres")
    @ToString.Exclude
    private String password;

    @NotNull(message = "El ID del rol es obligatorio") // Recibimos solo el ID
    private Long rolId;
}
