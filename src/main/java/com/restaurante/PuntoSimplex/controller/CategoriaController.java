package com.restaurante.PuntoSimplex.controller;

import com.restaurante.PuntoSimplex.Dto.ActualizarCategoriaRequestDTO;
import com.restaurante.PuntoSimplex.Dto.CrearCategoriaRequestDTO;
import com.restaurante.PuntoSimplex.Model.Categoria;
import com.restaurante.PuntoSimplex.exception.CategoriaDuplicadaException;
import com.restaurante.PuntoSimplex.exception.CategoriaNoEncontradaException;
import com.restaurante.PuntoSimplex.service.CategoriaService; // 👈 Cambiado a Service
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService categoriaService; // 👈 Inyectamos el Service


    /**
     * Endpoint para crear una nueva categoría.
     */
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PostMapping
    @Operation(summary = "Crea una nueva categoría",
            description = "Requiere un nombre de descripción único.")
    public ResponseEntity<?> crearCategoria(@Valid @RequestBody CrearCategoriaRequestDTO request) {
        try {
            // Lógica movida al Service
            Categoria nuevaCategoria = categoriaService.crearCategoria(request);
            return new ResponseEntity<>(nuevaCategoria, HttpStatus.CREATED); // 201 Created

        } catch (CategoriaDuplicadaException e) {
            // 409 Conflict si el nombre ya existe
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (RuntimeException e) {
            // Manejo de otros posibles errores
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al crear la categoría: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PutMapping("/{id}") // <--- ASEGÚRATE DE QUE ESTÉ ASÍ
    @Operation(summary = "Actualiza una categoría existente",
            description = "Permite cambiar la descripción y el estado activo/inactivo por ID.")
    public ResponseEntity<?> actualizarCategoria(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarCategoriaRequestDTO request) {

        try {
            // Llama al servicio para ejecutar la lógica de actualización
            Categoria categoriaActualizada = categoriaService.actualizarCategoria(id, request);

            // Retorna la categoría actualizada.
            // NOTA: Se recomienda retornar un DTO (CategoriaResponseDTO) en lugar de la entidad Categoria.
            return ResponseEntity.ok(categoriaActualizada); // 200 OK

        } catch (CategoriaNoEncontradaException e) {
            // 404 Not Found si el ID no existe
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (CategoriaDuplicadaException e) {
            // 409 Conflict si la nueva descripción ya existe
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (RuntimeException e) {
            // Manejo de otros posibles errores
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al actualizar la categoría: " + e.getMessage());
        }
    }
    /**
     * Tarea I.3: Exponer el GET para listar categorías.
     */
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'CAJERO', 'MESERO')")
    @GetMapping // GET /api/categorias
    @Operation(summary = "Lista todas las categorías",
            description = "Devuelve una lista completa de todas las categorías disponibles.")
    public ResponseEntity<List<Categoria>> listarCategorias() {
        // Lógica movida al Service
        List<Categoria> categorias = categoriaService.listarTodasLasCategorias();
        return ResponseEntity.ok(categorias); // 200 OK
    }

    // El método PUT para actualizar se implementará más adelante.


}