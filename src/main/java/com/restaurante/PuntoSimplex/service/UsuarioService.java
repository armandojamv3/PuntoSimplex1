package com.restaurante.PuntoSimplex.service;

import com.restaurante.PuntoSimplex.Dto.UsuarioDto;
import com.restaurante.PuntoSimplex.Model.Usuario;
import com.restaurante.PuntoSimplex.repository.UsuarioRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service // Define esta clase como servicio, parte de la capa de negocio.
@RequiredArgsConstructor // Genera automáticamente un constru con los atribs final (en este caso, usuarioRepository).

public class UsuarioService {


    //@Autowired

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;


    // Listar todos los usuarios
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    //  Buscar usuario por ID
    public Optional<Usuario> obtenerUsuarioPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    //  Crear un nuevo usuario
    public Usuario crearUsuario(Usuario usuario) {
        if (usuario.getActivo() == null) {
            usuario.setActivo(true); // si no se envía el campo, se crea como activo por defecto
            usuario.setPassword(usuario.getPassword());
        }
        // 1. Cifrar y asignar el hash cifrado en un solo paso
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword())); // <--- ¡Esta es la clave!


        // Aquí podrías encriptar la contraseña antes de guardar
        return usuarioRepository.save(usuario);
    }

    //  Actualizar usuario existente
    public Usuario actualizarUsuario(Long id, Usuario nuevosDatos) {
        return usuarioRepository.findById(id).map(usuario -> {
            usuario.setPrimerNombre(nuevosDatos.getPrimerNombre());
            usuario.setSegundoNombre(nuevosDatos.getSegundoNombre());
            usuario.setPrimerApellido(nuevosDatos.getPrimerApellido());
            usuario.setSegundoApellido(nuevosDatos.getSegundoApellido());
            usuario.setUsuario(nuevosDatos.getUsuario());
            usuario.setPassword(nuevosDatos.getPassword());
            usuario.setActivo(nuevosDatos.getActivo());
            usuario.setRol(nuevosDatos.getRol());
            return usuarioRepository.save(usuario);
        }).orElseThrow(() -> new RuntimeException(" Usuario no encontrado con ID: " + id));
    }

    //  Eliminar usuario
    public void eliminarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("️ No se puede eliminar: usuario no encontrado con ID: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    //  Buscar por nombre de usuario
    public Optional<Usuario> buscarPorNombreUsuario(String nombreUsuario) {
        return usuarioRepository.findByUsuario(nombreUsuario);
    }
}