package com.restaurante.PuntoSimplex.controller;

import com.restaurante.PuntoSimplex.Dto.CrearUsuarioRequestDTO;
import com.restaurante.PuntoSimplex.Dto.UsuarioDto;
import com.restaurante.PuntoSimplex.Model.Rol;
import com.restaurante.PuntoSimplex.Model.Usuario;
import com.restaurante.PuntoSimplex.repository.RolReposirory;
import com.restaurante.PuntoSimplex.repository.UsuarioRepository;
import com.restaurante.PuntoSimplex.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RequiredArgsConstructor //(de Lombok) genera un constructor automático para los atributos final.
@RestController  //recibirá peticiones HTTP (como GET, etc) y devolverá datos en formato JSON.
@RequestMapping("/api/usuarios") //rutas
// controla las peticiones http
public class UsuarioController {

    // Se inyecta UsuarioService por constructor gracias a @RequiredArgsConstructor
    private final UsuarioService usuarioService;

    //  Inyecta el RolReposirory
    private final RolReposirory rolReposirory;



    // DTO( Data Transfer Object )
    // Listar todos los usuarios
    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    // Se usan @RequestParam para recibir los filtros como parámetros de URL
    public ResponseEntity<List<UsuarioDto>> getAllUsuarios(
            // Nombre (String): Será LIKE, insensible a mayúsculas. Es opcional (required = false).
            @RequestParam(required = false) String nombre,
            // Rol ID (Long): Filtra por el ID de rol. Es opcional.
            @RequestParam(required = false) Long rolId,
            // Activo (Boolean): Filtra por estado. Es opcional.
            @RequestParam(required = false) Boolean activo)
     {

         // 1. Llamar al nuevo método del Service con los parámetros de filtro
        List<Usuario> usuarios = usuarioService.listarUsuarios( nombre,rolId,activo);

         // 2. Mapear las entidades Usuario a los DTOs para la respuesta
         List<UsuarioDto> usuariosDto = usuarios.stream()
                 .map(UsuarioDto::fromUsuario)
                 .collect(Collectors.toList());
        return ResponseEntity.ok(usuariosDto);

         // NOTA: Se ha modificado el tipo de retorno a List<UsuarioDto> para mejorar la seguridad
         // y usar la lógica de mapeo que ya tienes definida




    }



    /**
     * Mapeo POST para crear un nuevo usuario (Cajero o Mesero).
     * La validación de unicidad de usuario y Rol se realiza en el Service.
     */
    @PreAuthorize("hasRole('ADMINISTRADOR')") // Solo el ADMIN puede crear usuarios (tabla rol)
    @PostMapping

    public ResponseEntity<?> createUsuario(@Valid @RequestBody CrearUsuarioRequestDTO request) {

        try {
            // Llama al Service con el DTO
            Usuario nuevo = usuarioService.crearUsuarioDesdeDto(request);

            // Devuelve 201 Created y el DTO de respuesta (UsuarioDto)
            return new ResponseEntity<>(UsuarioDto.fromUsuario(nuevo), HttpStatus.CREATED);

        } catch (IllegalArgumentException e) {

            if (e.getMessage().contains("ya está en uso")) {
                // Mensaje sugerido por el usuario:
                String mensajeError = "Ya existe un usuario con este alias.";

                return ResponseEntity.status(HttpStatus.CONFLICT) // 409 Conflict
                        .body(mensajeError);
        }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        catch (RuntimeException e) {
            // Manejo genérico de otras RuntimeExceptions (ej: Rol no encontrado)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }



    // 🔹 Obtener un usuario por ID
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getUsuarioById(@PathVariable Long id) {
        return usuarioService.obtenerUsuarioPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 🔹 Actualizar un usuario
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> updateUsuario(@PathVariable Long id, @Valid @RequestBody Usuario usuarioDetails) {
        try {
            Usuario actualizado = usuarioService.actualizarUsuario(id, usuarioDetails);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }



    // 🔹 Eliminar un usuario
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Long id) {
        try {
            usuarioService.eliminarUsuario(id);
            return ResponseEntity.noContent().build(); // 204
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    //  Buscar usuario por nombre
    @GetMapping("/buscar/{nombreUsuario}")
    public ResponseEntity<Usuario> buscarPorNombre(@PathVariable String nombreUsuario) {
        return usuarioService.buscarPorNombreUsuario(nombreUsuario)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }



    // 🔹 Endpoint para listar todos los roles

    // Se requiere un rol para acceder (probablemente solo ADMINISTRADOR)
    @GetMapping("/roles")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<List<Rol>> getAllRoles() {

        // 1. Llama al método findAll() de JpaRepository
        List<Rol> roles = rolReposirory.findAll();

        // 2. Devuelve la lista de roles con un estado 200 OK
        return ResponseEntity.ok(roles);
    }
}