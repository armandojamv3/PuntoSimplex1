package com.restaurante.PuntoSimplex.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;
import org.springframework.stereotype.Service;

@Entity
@Data
@Table(name = "usuario")
// almacena y define la estrutura de los datos
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;


    @NotBlank(message = "El primer nombre del usuario es obligatorio")
    @Size(max = 20, message = "El nombre no puede tener más de 20 caracteres")
    @Column(name = "primer_nombre",nullable = false, length = 20)
    private String primerNombre;

    @NotBlank(message = "El segundo nombre del usuario es obligatorio")
    @Size(max = 20, message = "El segundo nombre no puede tener más de 20 caracteres")
    @Column(name = "segundo_nombre",length = 20)
    private String segundoNombre;

    @NotBlank(message = "El primer Apellido  es obligatorio")
    @Size(max = 20, message = " El apellido no debe tener mas de 20 caracteres")
    @Column(name = "primer_apellido",length = 20)
    private String primerApellido;

    @NotBlank(message = "El primer Apellido  es obligatorio")
    @Size(max = 20, message = " El apellido no debe tener mas de 20 caracteres")
    @Column(name = "segundo_apellido",length = 20)
    private String segundoApellido;


    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(max = 20, message = "El usuario no puede tener más de 20 caracteres")
    @Column(name = "usuario",nullable = false, unique = true)
    private String usuario;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(max = 60, message = "La contraseña no puede tener más de 60 caracteres")
    @ToString.Exclude // Evita mostrar la contraseña en logs
    @Column(name = "password",nullable = false,unique = false,length = 60)
    private String password;

    @Column(name = "activo",nullable = false)
    private  Boolean  activo;



    // 🔗 Relación con la tabla Rol
    // @ManyToOne Indica que muchos usuarios pueden tener un mismo rol.
    @ManyToOne(fetch = FetchType.EAGER) //Esto indica que cada vez que obtengas un usuario, Hibernate también traerá automáticamente su rol.
    @JoinColumn(name = "rol_id",nullable = false) //Le dice a Hibernate que en la tabla usuario habrá una columna llamada rol_id.
    private Rol rol; //anidado
}
