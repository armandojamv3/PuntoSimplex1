package com.restaurante.PuntoSimplex.controller;

import com.restaurante.PuntoSimplex.Dto.UsuarioDto;
import com.restaurante.PuntoSimplex.Model.Usuario;
import com.restaurante.PuntoSimplex.repository.UsuarioRepository;
import com.restaurante.PuntoSimplex.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;



@RequiredArgsConstructor
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {


    //llamamos la clase UsuarioService
     private final UsuarioService usuarioService;


    @PostMapping("/registro")
    public ResponseEntity registrarUsuario(@Valid @RequestBody UsuarioDto dto) {
        usuarioService.registrarUsuario(dto);
        return ResponseEntity.ok(Map.of("mensaje", "Usuario registrado con éxito."));
    }
}
