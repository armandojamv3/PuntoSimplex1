package com.restaurante.PuntoSimplex.controller;

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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;



@RequiredArgsConstructor //(de Lombok) genera un constructor automático para los atributos final.
@RestController  //recibirá peticiones HTTP (como GET, etc) y devolverá datos en formato JSON.
@RequestMapping("/api/usuarios") //rutas
public class UsuarioController {

    private final UsuarioService usuarioService;


    // Listar todos los usuarios
    @GetMapping
    public ResponseEntity<List<Usuario>> getAllUsuarios() {
        List<Usuario> usuarios = usuarioService.listarUsuarios();
        return ResponseEntity.ok(usuarios);
    }


    @Autowired

    private RolReposirory rolReposirory;
    // Crear un nuevo usuario
    @PostMapping
    public ResponseEntity<Usuario> createUsuario(@Valid @RequestBody Usuario usuario) {

        // Validar que el rol no sea nulo y que el rol_id no sea nulo
        if (usuario.getRol() == null || usuario.getRol().getRol_id() == null) {
            throw new RuntimeException("Rol no proporcionado o rol_id es nulo");
        }

        Rol rolExistente = rolReposirory.findById(usuario.getRol().getRol_id())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        usuario.setRol(rolExistente); // asignar el rol recuperado
        Usuario nuevo = usuarioService.crearUsuario(usuario);
        return ResponseEntity.ok(nuevo);
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
}