package com.restaurante.PuntoSimplex.service;

import com.restaurante.PuntoSimplex.Dto.ProductoResponseDTO;
import com.restaurante.PuntoSimplex.Model.Inventario;
import com.restaurante.PuntoSimplex.Model.Producto;
import com.restaurante.PuntoSimplex.exception.StockInsuficienteException;
import com.restaurante.PuntoSimplex.repository.InventarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class InventarioService {

    private final InventarioRepository inventarioRepository;
    private final ProductoService productoService;

    // ----------------------------------------------------------------------
    // HU 5.1: MÉTODO PARA SUMAR (REGISTRAR ENTRADA) - LÓGICA DE ENTRADA
    // ----------------------------------------------------------------------

    @Transactional
    public ProductoResponseDTO registrarEntradaStock(Long productoId, Integer cantidad) {

        // 1. Validar la cantidad
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad de stock a agregar debe ser mayor a cero.");
        }

        // 2. Buscar Producto e Inventario
        Producto producto = productoService.findById(productoId);
        Inventario inventario = inventarioRepository.findByProductoId(producto.getId())
                .orElseThrow(() -> new RuntimeException("Error interno: No se encontró registro de inventario para el producto ID: " + producto.getId()));

        // 3. Lógica de NEGOCIO: SUMA (Entrada)
        int nuevoStock = inventario.getStockActual() + cantidad;

        inventario.setStockActual(nuevoStock);
        inventario.setUltimaActualizacion(LocalDateTime.now());

        // 4. Guardar y Retornar
        inventarioRepository.save(inventario);
        return productoService.mapToProductoResponseDTO(producto);
    }

    // ----------------------------------------------------------------------
    // HU 5.2: MÉTODO PARA RESTAR (REGISTRAR SALIDA) - NUEVA IMPLEMENTACIÓN
    // ----------------------------------------------------------------------

    /**
     * HU 5.2: Registra la salida de stock (restar cantidad), usada en las ventas.
     */
    @Transactional
    public ProductoResponseDTO registrarSalidaStock(Long productoId, Integer cantidad) { // 👈 MÉTODO REQUERIDO

        // 1. Validar la cantidad a restar
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad de stock a retirar debe ser mayor a cero.");
        }

        // 2. Buscar Producto e Inventario
        Producto producto = productoService.findById(productoId);
        Inventario inventario = inventarioRepository.findByProductoId(producto.getId())
                .orElseThrow(() -> new RuntimeException("Error interno: No se encontró registro de inventario para el producto ID: " + producto.getId()));

        // 3. Lógica de NEGOCIO: RESTA y Validación (Salida)
        int nuevoStock = inventario.getStockActual() - cantidad;

        // 3.1. Validación de stock suficiente
        if (nuevoStock < 0) {
            throw new StockInsuficienteException(
                    "Stock insuficiente para el producto '" + producto.getNombre() +
                            "'. Stock actual: " + inventario.getStockActual() + ", Cantidad solicitada: " + cantidad
            );
        }

        // 3.2. Actualizar Stock
        inventario.setStockActual(nuevoStock);
        inventario.setUltimaActualizacion(LocalDateTime.now());

        // 4. Guardar y Retornar
        inventarioRepository.save(inventario);
        return productoService.mapToProductoResponseDTO(producto);
    }

    // ----------------------------------------------------------------------
    // MÉTODO PARA CREAR ENTRADA INICIAL (Si no existe, el compilador fallará)
    // ----------------------------------------------------------------------
    /*
     * Asegúrate de tener este método si es llamado por ProductoService:
     * * public void crearEntradaInicial(Producto producto, Integer cantidad) {
     * // ... (Implementación usando inventarioRepository)
     * }
     */
}