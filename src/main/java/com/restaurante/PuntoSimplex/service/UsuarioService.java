package com.restaurante.PuntoSimplex.service;

import com.restaurante.PuntoSimplex.Dto.UsuarioDto;
import com.restaurante.PuntoSimplex.Model.Usuario;
import com.restaurante.PuntoSimplex.repository.UsuarioRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@Service // Define esta clase como servicio, parte de la capa de negocio.
@RequiredArgsConstructor // Genera automáticamente un constru con los atribs final (en este caso, usuarioRepository).

public class UsuarioService {

    private final UsuarioRepository usuarioRepository; // usuarioRepository nombre variable, llamar Los metdos BD

   // retorna Usuario el que se creo
    public Usuario registrarUsuario( UsuarioDto dto) {

        // crea un nuevo object usuario
        Usuario usuario = new Usuario();

        //Copia los datos del DTO al objeto Usuario
        usuario.setUsername(dto.getUsername());
        usuario.setPassword(dto.getPassword());

        //  Guarda el usuario en la base de datos y retorna el objeto guardado
        return usuarioRepository.save(usuario);
    }
}
