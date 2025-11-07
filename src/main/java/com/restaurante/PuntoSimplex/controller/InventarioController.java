package com.restaurante.PuntoSimplex.controller;

import com.restaurante.PuntoSimplex.Dto.ProductoResponseDTO;
import com.restaurante.PuntoSimplex.Dto.RegistrarEntradaStockRequestDTO;
import com.restaurante.PuntoSimplex.exception.ProductoNoEncontradoException;
import com.restaurante.PuntoSimplex.exception.StockInsuficienteException;
import com.restaurante.PuntoSimplex.service.InventarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/inventario") // Nueva ruta para el control de inventario
@RequiredArgsConstructor
@Tag(name = "Inventario", description = "Gestión de las entradas y salidas de Stock (HU 5)")
public class InventarioController {

    private final InventarioService inventarioService;


    /**
     * HU 5.1: Endpoint para registrar la entrada de stock (sumar cantidad) a un producto.
     * Restringido a ADMINISTRADOR.
     */
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PostMapping("/entrada") // POST /api/inventario/entrada
    @Operation(summary = "Registra una entrada de stock",
            description = "Suma la 'cantidad' especificada al stock actual del 'productoId' dado.")
    public ResponseEntity<?> registrarEntradaStock(@Valid @RequestBody RegistrarEntradaStockRequestDTO request) {

        try {
            // Llama al servicio para ejecutar la lógica de negocio (sumar stock)
            ProductoResponseDTO productoActualizadoDTO = inventarioService.registrarEntradaStock(
                    request.getProductoId(),
                    request.getCantidad()
            );

            // Retorna el DTO del producto con el stock actualizado
            return ResponseEntity.status(HttpStatus.OK).body(productoActualizadoDTO); // 200 OK

        } catch (ProductoNoEncontradoException e) {
            // 404 Not Found si el ID del producto no existe
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (RuntimeException e) {
            // Manejo genérico de errores (ej: si la cantidad es inválida)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @Transactional
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'CAJERO')")
    @PostMapping("/salida") // POST /api/inventario/salida
    @Operation(summary = "Registra una salida de stock",
            description = "Resta la 'cantidad' especificada del stock actual del 'productoId' dado (usado para registrar ventas).")

    public ResponseEntity<?> registrarSalidaStock(@Valid @RequestBody RegistrarEntradaStockRequestDTO request) {

        try {
            // Llama al servicio para ejecutar la lógica de negocio (restar stock)
            ProductoResponseDTO productoActualizadoDTO = inventarioService.registrarSalidaStock(
                    request.getProductoId(),
                    request.getCantidad()
            );

            // Retorna el DTO del producto con el stock actualizado
            return ResponseEntity.status(HttpStatus.OK).body(productoActualizadoDTO); // 200 OK

        } catch (ProductoNoEncontradoException e) {
            // 404 Not Found si el ID del producto no existe
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

        } catch (StockInsuficienteException e) {
            // 400 Bad Request si no hay suficiente stock para la venta
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

        } catch (RuntimeException e) {
            // Manejo genérico para otros errores
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    // Pendiente de implementar HU 5.2: Registrar Salida de Stock (si se requiere una salida manual)
    // Pendiente de implementar HU 5.3: Obtener Historial de Movimientos
}