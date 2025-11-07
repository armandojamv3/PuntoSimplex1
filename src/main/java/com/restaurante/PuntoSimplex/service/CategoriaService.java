package com.restaurante.PuntoSimplex.service;

import com.restaurante.PuntoSimplex.Dto.ActualizarCategoriaRequestDTO; // 👈 Añadido
import com.restaurante.PuntoSimplex.Dto.CategoriaResponseDTO;     // 👈 Añadido
import com.restaurante.PuntoSimplex.Dto.CrearCategoriaRequestDTO;
import com.restaurante.PuntoSimplex.Model.Categoria;
import com.restaurante.PuntoSimplex.exception.CategoriaDuplicadaException;
import com.restaurante.PuntoSimplex.exception.CategoriaNoEncontradaException; // 👈 Añadido
import com.restaurante.PuntoSimplex.repository.CategoriaRepository;
import jakarta.transaction.Transactional; // 👈 Añadido
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    /**
     * Tarea I.1: Mueve la lógica de creación y validación de unicidad al Service.
     */
    public Categoria crearCategoria(CrearCategoriaRequestDTO request) {
        if (categoriaRepository.findByDescripcion(request.getDescripcion()).isPresent()) {
            throw new CategoriaDuplicadaException(
                    "La categoría '" + request.getDescripcion() + "' ya existe. Por favor, use otra descripción."
            );
        }

        Categoria categoria = new Categoria();
        categoria.setDescripcion(request.getDescripcion());
        // ASUMIMOS que el modelo Categoria tiene 'activo' y se inicializa en true
        // Si no lo tiene, esta línea podría causar un error.
         categoria.setActivo(true);

        return categoriaRepository.save(categoria);
    }

    /**
     * Tarea I.3: Listar Categorías (por ahora sin filtros).
     */
    public List<Categoria> listarTodasLasCategorias() {
        return categoriaRepository.findAll();
    }

    /**
     * Tarea I.4: Implementar la lógica para actualizar una categoría.
     */
    @Transactional
    public Categoria actualizarCategoria(Long id, ActualizarCategoriaRequestDTO request) {

        // 1. Buscar la Categoría existente por ID (lanza 404 si no existe)
        Categoria categoriaExistente = categoriaRepository.findById(id)
                .orElseThrow(() -> new CategoriaNoEncontradaException(
                        "Categoría no encontrada con ID: " + id
                ));

        // 2. Validación de Unicidad (solo si la descripción CAMBIA)
        if (!categoriaExistente.getDescripcion().equalsIgnoreCase(request.getDescripcion())) {

            // Busca si la nueva descripción ya está ocupada por CUALQUIER otra categoría
            if (categoriaRepository.findByDescripcion(request.getDescripcion()).isPresent()) {
                throw new CategoriaDuplicadaException(
                        "El nombre de la categoría '" + request.getDescripcion() + "' ya está registrado."
                );
            }
        }

        // 3. Aplicar Cambios
        categoriaExistente.setDescripcion(request.getDescripcion());
        // Se asume que tu modelo Categoria tiene el campo 'activo'
        categoriaExistente.setActivo(request.getActivo());

        // 4. Guardar los cambios (Actualizar)
        return categoriaRepository.save(categoriaExistente);
    }

    /**
     * Método auxiliar (mapper) para convertir Entidad a DTO de Respuesta
     */
    public CategoriaResponseDTO mapToCategoriaResponseDTO(Categoria categoria) {
        return CategoriaResponseDTO.builder()
                .id(categoria.getId())
                .descripcion(categoria.getDescripcion())
                // Asumiendo que tu modelo Categoria tiene el campo 'activo'
                .activo(categoria.getActivo())
                .build();
    }

    // Código CORREGIDO en CategoriaService.java
    public List<CategoriaResponseDTO> obtenerTodasLasCategorias() {
        // 1. Busca todas las entidades Categoria
        List<Categoria> categorias = categoriaRepository.findAll();

        // 2. Mapea la lista de entidades a la lista de DTOs usando el Builder
        return categorias.stream()
                // Usa el patrón Builder para crear la instancia del DTO
                .map(cat -> CategoriaResponseDTO.builder()
                        .id(cat.getId())
                        .descripcion(cat.getDescripcion())
                        .activo(cat.getActivo())
                        .build())
                .collect(Collectors.toList());
    }
}