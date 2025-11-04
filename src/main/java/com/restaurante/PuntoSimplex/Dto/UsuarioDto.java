package com.restaurante.PuntoSimplex.Dto;

import lombok.Builder;
import lombok.Data;


@Builder
@Data
public class UsuarioDto {
    //private String username;
    //private String password;

    private Long id;
    private String primerNombre;
    private String segundoNombre;
    private String primerApellido;
    private String segundoApellido;
    private String usuario;
    private Boolean activo;

    // Solo se expone la descripción del rol (ADMINISTRADOR, CAJERO, etc.)
    private String rolDescripcion;

    /**
     * Método estático de utilidad para transformar un objeto Usuario (Model)
     * en un objeto UsuarioListadoDTO.
     * @param usuario El objeto Usuario de la base de datos.
     * @return El DTO listo para ser enviado en la respuesta.
     */
    public static UsuarioDto fromUsuario(com.restaurante.PuntoSimplex.Model.Usuario usuario) {
        // Aseguramos que el rol exista antes de intentar obtener su descripción
        String descripcion = (usuario.getRol() != null) ? usuario.getRol().getDescripcion() : "SIN_ROL";

        return UsuarioDto
                .builder()
                .id(usuario.getId())
                .primerNombre(usuario.getPrimerNombre())
                .segundoNombre(usuario.getSegundoNombre())
                .primerApellido(usuario.getPrimerApellido())
                .segundoApellido(usuario.getSegundoApellido())
                .usuario(usuario.getUsuario())
                .activo(usuario.getActivo())
                .rolDescripcion(descripcion)
                .build();
    }
}
