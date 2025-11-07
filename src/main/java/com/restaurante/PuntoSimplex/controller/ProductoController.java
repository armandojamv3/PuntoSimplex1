package com.restaurante.PuntoSimplex.controller;

import com.restaurante.PuntoSimplex.Dto.ActualizarProductoRequestDTO;
import com.restaurante.PuntoSimplex.Dto.CrearProductoRequestDTO;
import com.restaurante.PuntoSimplex.Dto.ProductoResponseDTO;
import com.restaurante.PuntoSimplex.Model.Producto;
import com.restaurante.PuntoSimplex.exception.ProductoDuplicadoException;
import com.restaurante.PuntoSimplex.exception.ProductoNoEncontradoException;
import com.restaurante.PuntoSimplex.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/productos")// Nueva ruta para productos


public class ProductoController {
    private final ProductoService productoService;

    /**
     * Endpoint para crear un nuevo producto.
     */
    @PreAuthorize("hasRole('ADMINISTRADOR')") // Asumiendo restricción de seguridad
    @PostMapping
    public ResponseEntity<?> createProducto(@Valid @RequestBody CrearProductoRequestDTO request) {

        try {
            // Llama al Service, donde ocurre toda la lógica de negocio y guardado.
            Producto nuevoProducto = productoService.crearProductoDesdeDto(request);

            ProductoResponseDTO responseDTO = productoService.obtenerProductoPorId(nuevoProducto.getId());

            // 7.1 y 7.3: Éxito
            // Asume que tienes un ProductoDto.fromProducto(nuevoProducto) para la respuesta
            // Si no lo tienes, puedes devolver la entidad directamente o un mensaje.
            return new ResponseEntity<>(nuevoProducto, HttpStatus.CREATED); // 201 Created

        } catch (ProductoDuplicadoException e) {
            // 6.2: Maneja el error de unicidad (Nombre ya existe)
            return ResponseEntity.status(HttpStatus.CONFLICT) // 409 Conflict
                    .body(e.getMessage());

        } catch (RuntimeException e) {
            // Manejo genérico para otros errores de negocio (ej: Categoría no encontrada)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al crear el producto:" + e.getMessage());
        }


    }


    @GetMapping // Maneja GET /api/productos
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'CAJERO', 'MESERO')") // Define roles de acceso
    @Operation(summary = "Lista todos los productos activos",
            description = "Permite filtrar por nombre parcial y/o ID de categoría. Por defecto, solo trae productos activos.")

    public ResponseEntity<List<ProductoResponseDTO>> listarProductos(
            // Parámetros de filtro: opcionales
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) Long categoriaId
    ) {
        // Llama al servicio con los parámetros de filtro
        List<ProductoResponseDTO> productos = productoService.listarProductos(nombre, categoriaId);

        // Retorna la lista con un código 200 OK
        return ResponseEntity.ok(productos);
    }


    // --- HU 4.3: Desactivar Producto (PUT /productos/{id}/desactivar) ---

    @PutMapping("/{id}/desactivar")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @Operation(summary = "Desactiva un producto",
            description = "Marca un producto como inactivo. Requiere ID del producto.")
    public ResponseEntity<?> desactivarProducto(@PathVariable Long id) {
        return actualizarEstado(id, false);
    }

    // --- HU 4.3: Activar Producto (PUT /productos/{id}/activar) ---

    // 💡 MÉTODO AGREGADO: Endpoint para Activar Producto
    @PutMapping("/{id}/activar")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @Operation(summary = "Activa un producto",
            description = "Marca un producto como activo, haciéndolo disponible para la venta. Requiere ID del producto.")
    public ResponseEntity<?> activarProducto(@PathVariable Long id) {
        return actualizarEstado(id, true);
    }


    // --- HU: Actualizar Producto (PUT /productos/{id}) ---

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PutMapping("/{id}") // PUT /api/productos/{id}
    @Operation(summary = "Actualiza un producto existente",
            description = "Permite modificar el nombre, precios, categoría y estado (activo).")
    public ResponseEntity<?> actualizarProducto(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarProductoRequestDTO request) {

        try {
            // Llama al servicio para ejecutar la lógica de actualización
            ProductoResponseDTO productoActualizadoDTO = productoService.actualizarProducto(id, request);

            // Retorna 200 OK con el DTO actualizado
            return ResponseEntity.ok(productoActualizadoDTO);

        } catch (ProductoNoEncontradoException e) {
            // Manejo: Producto no encontrado (ID inválido)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); // 404 Not Found

        } catch (ProductoDuplicadoException e) {
            // Manejo: Nombre ya existe en otro producto
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage()); // 409 Conflict

        } catch (RuntimeException e) {
            // Manejo genérico para errores (ej: Categoría no encontrada, validación de DTO)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al actualizar: " + e.getMessage()); // 400 Bad Request
        }
    }

    // 💡 MÉTODO AGREGADO: Auxiliar privado para actualizar el estado
    /**
     * Endpoint privado para cambiar el estado (activo/inactivo) de un producto.
     * Llamado por /desactivar y /activar.
     */
    private ResponseEntity<?> actualizarEstado(Long id, boolean activo) {
        try {
            // 1. Llama al servicio para actualizar el estado
            Producto productoActualizado = productoService.actualizarEstado(id, activo);

            // 2. Retorna el DTO actualizado (incluye el estado 'activo' y 'stock')
            ProductoResponseDTO responseDTO = productoService.obtenerProductoPorId(productoActualizado.getId());

            return ResponseEntity.ok(responseDTO);

        } catch (ProductoNoEncontradoException e) {
            // 404 Not Found si el ID no existe
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (RuntimeException e) {
            // Manejo genérico de errores
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar el estado: " + e.getMessage());
        }
    }

    /**
     * Tarea 5: Endpoint para obtener los detalles de un solo producto por ID.
     * Es crucial para que el front-end cargue el formulario de edición.
     */
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'CAJERO', 'MESERO')") // Roles que necesitan ver el detalle
    @GetMapping("/{id}") // GET /api/productos/{id}
    @Operation(summary = "Obtiene los detalles de un producto por ID",
            description = "Devuelve el detalle completo del producto, incluyendo el stock actual.")
    public ResponseEntity<?> obtenerProductoPorId(@PathVariable Long id) {
        try {
            // Llama al servicio que ya creaste para obtener el DTO
            ProductoResponseDTO responseDTO = productoService.obtenerProductoPorId(id);

            // Retorna 200 OK
            return ResponseEntity.ok(responseDTO);

        } catch (ProductoNoEncontradoException e) {
            // Manejo: Producto no encontrado (ID inválido)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); // 404 Not Found

        } catch (RuntimeException e) {
            // Manejo genérico
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener el producto: " + e.getMessage());
        }
    }
}