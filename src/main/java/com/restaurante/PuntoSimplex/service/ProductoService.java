package com.restaurante.PuntoSimplex.service;

import com.restaurante.PuntoSimplex.Dto.ActualizarProductoRequestDTO;
import com.restaurante.PuntoSimplex.Dto.CrearProductoRequestDTO;
import com.restaurante.PuntoSimplex.Dto.ProductoResponseDTO;
import com.restaurante.PuntoSimplex.Model.Categoria;
import com.restaurante.PuntoSimplex.Model.Inventario;
import com.restaurante.PuntoSimplex.Model.Producto;
import com.restaurante.PuntoSimplex.exception.ProductoDuplicadoException;
import com.restaurante.PuntoSimplex.exception.ProductoNoEncontradoException;
import com.restaurante.PuntoSimplex.repository.CategoriaRepository;
import com.restaurante.PuntoSimplex.repository.InventarioRepository;
import com.restaurante.PuntoSimplex.repository.ProductoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
  //  private final InventarioService inventarioService; // Para Requisito 8.4
    private final InventarioRepository inventarioRepository;

    @Transactional
    public Producto crearProductoDesdeDto(CrearProductoRequestDTO request) {

        // 1. Requisito 6.1 / 8.3: Validación de unicidad (nombre no duplicado)
        if (productoRepository.findByNombreIgnoreCase(request.getNombre()).isPresent()) {
            throw new ProductoDuplicadoException("El producto ya está registrado, por favor use otro nombre.");
        }

        // 2. Requisito 4.2 / 8.1: Búsqueda de Categoría
        Categoria categoria = categoriaRepository.findById(request.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + request.getCategoriaId()));

        // 3. Mapeo de DTO a Entidad Producto
        Producto producto = new Producto();
        producto.setNombre(request.getNombre());
        producto.setPrecioVenta(request.getPrecioVenta());
        producto.setCategoria(categoria); // Asigna la entidad Categoria

        producto.setPrecioFloat(request.getPrecioVenta());

        // Por defecto, un producto recién creado siempre debe estar activo (HU 4.3)
        producto.setActivo(true);

        // 4. Guardar Producto
        Producto productoGuardado = productoRepository.save(producto);

        // 5. Requisito 8.4: Integridad Referencial
        this.crearEntradaInicial(productoGuardado, request.getStockInicial());

        return productoGuardado;
    }

    public void crearEntradaInicial(Producto producto, Integer cantidad) {

        Inventario inventario = new Inventario();
        inventario.setProducto(producto);
        inventario.setStockActual(cantidad); // Usa la cantidad inicial del DTO
        inventario.setUltimaActualizacion(LocalDateTime.now());

        inventarioRepository.save(inventario);
    }

    /**
     * Método auxiliar para mapear la entidad Producto a ProductoResponseDTO.
     */
     ProductoResponseDTO mapToProductoResponseDTO(Producto producto) {
        // 1. Obtener el Stock actual consultando la tabla de inventario
        // REQUIERE QUE InventarioRepository TENGA findByProductoId
        Integer stock = inventarioRepository.findByProductoId(producto.getId())
                .map(Inventario::getStockActual)
                .orElse(0);

        // 2. Mapear a DTO (Asumiendo que has creado ProductoResponseDTO)
        return ProductoResponseDTO.builder()
                .nombre(producto.getNombre())
                .precioVenta(producto.getPrecioVenta())
                .stock(stock)
                .categoriaId(producto.getCategoria().getId())
                .build();
    }

    public Producto findById(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ProductoNoEncontradoException("Producto no encontrado con ID: " + id));
    }

    @Transactional
    public Producto actualizarEstado(Long id, boolean activo) {
        // 1. Buscar el producto. Si no existe, lanzar excepción
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ProductoNoEncontradoException("Producto no encontrado con ID: " + id));

        // 2. Actualizar el campo 'activo'
        producto.setActivo(activo);

        // 3. Guardar y retornar
        return productoRepository.save(producto);
    }



    // =========================================================================
    // 💡 NUEVO MÉTODO COMPLETO: HU - Actualizar Producto
    // =========================================================================

    @Transactional
    public ProductoResponseDTO actualizarProducto(Long id, ActualizarProductoRequestDTO request) {

        // 1. Buscar el Producto existente por ID (lanza 404 si no existe)
        Producto productoExistente = findById(id);

        // 2. Validación de Unicidad para Actualización (HU 3)
        // Solo validamos si el nombre CAMBIA y si ese nuevo nombre YA EXISTE para OTRO producto.
        if (!productoExistente.getNombre().equalsIgnoreCase(request.getNombre())) {

            // Busca si el nuevo nombre ya está ocupado por CUALQUIER otro producto
            if (productoRepository.findByNombreIgnoreCase(request.getNombre()).isPresent()) {
                throw new ProductoDuplicadoException(
                        "El nombre '" + request.getNombre() +
                                "' ya está registrado en otro producto."
                );
            }
        }

        // 3. Validar que la Categoría exista
        Categoria categoria = categoriaRepository.findById(request.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + request.getCategoriaId()));

        // 4. Mapear y Aplicar Cambios
        productoExistente.setNombre(request.getNombre());
        productoExistente.setPrecioVenta(
                java.math.BigDecimal.valueOf(request.getPrecioVenta())
        );
        //  OJO: Si tienes precioCompra en el DTO, descomenta y usa esta línea:
        // productoExistente.setPrecioCompra(request.getPrecioCompra());
        productoExistente.setCategoria(categoria);
        productoExistente.setActivo(request.getActivo());

        // 5. Guardar los cambios (Actualizar)
        Producto productoActualizado = productoRepository.save(productoExistente);

        // 6. Retornar el DTO con el stock actual
        return mapToProductoResponseDTO(productoActualizado);
    }



    /**
     * Provee la lista de productos, permitiendo filtros opcionales por nombre y categoría.
     * (Método principal de la HU)
     */
    public List<ProductoResponseDTO> listarProductos(String nombre, Long categoriaId) {
        List<Producto> productos;

        // Lógica de filtrado que usa los nuevos métodos de ProductoRepository
        if (nombre != null && !nombre.trim().isEmpty() && categoriaId != null) {
            productos = productoRepository.findByNombreContainingIgnoreCaseAndCategoriaId(nombre, categoriaId).stream()
                    .filter(Producto::getActivo)
                    .collect(Collectors.toList());

        } else if (nombre != null && !nombre.trim().isEmpty()) {
            productos = productoRepository.findByNombreContainingIgnoreCaseAndActivoTrue(nombre);
        } else if (categoriaId != null) {
            productos = productoRepository.findByCategoriaId(categoriaId).stream()
                    .filter(Producto::getActivo)
                    .collect(Collectors.toList());
        } else {
            // 4. Sin filtros: lista completa, pero SOLO ACTIVOS
            productos = productoRepository.findByActivoTrue();  // 👈 Usamos el nuevo método findByActivoTrue()
        }

        // Mapea y retorna la lista
        return productos.stream()
                .map(this::mapToProductoResponseDTO)
                .collect(Collectors.toList());




    }
    // Método para obtener un DTO de un producto específico, útil para detalles o edición.
    public ProductoResponseDTO obtenerProductoPorId (Long id){
        Producto producto = findById(id);
        return mapToProductoResponseDTO(producto);
    }
}



