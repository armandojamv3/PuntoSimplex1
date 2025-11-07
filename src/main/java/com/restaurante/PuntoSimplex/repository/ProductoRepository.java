package com.restaurante.PuntoSimplex.repository;



import com.restaurante.PuntoSimplex.Model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
// La interfaz extiende de JpaRepository para obtener métodos CRUD listos para usar
@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    Optional<Producto> findByNombreIgnoreCase(String nombre);


    // 1. Filtrar por Nombre Y Categoría
    // Usamos el ID de la Categoría, ya que la relación es Many-to-One
    List<Producto> findByNombreContainingIgnoreCaseAndCategoriaId(String nombre, Long categoriaId);

    // 2. Solo filtrar por Nombre
    List<Producto> findByNombreContainingIgnoreCase(String nombre);

    // 3. Solo filtrar por Categoría
    // Usamos el ID de la Categoría
    List<Producto> findByCategoriaId(Long categoriaId);

    // Sobreescribir findAll para que solo traiga activos (usando Convención de Nombre)
    List<Producto> findByActivoTrue(); //  Nuevo método para findAll()

    // 2. Método para buscar por nombre (sensible a mayúsculas/minúsculas) y que SÓLO traiga activos.
    List<Producto> findByNombreContainingIgnoreCaseAndActivoTrue(String nombre);


    // 1. Filtrar por Nombre Y Categoría Y Activo (Nuevo método recomendado)
    // Usamos el ID de la Categoría, ya que la relación es Many-to-One
    List<Producto> findByNombreContainingIgnoreCaseAndCategoriaIdAndActivoTrue(String nombre, Long categoriaId);

    // 3. Solo filtrar por Categoría Y Activo (Nuevo método recomendado)
    List<Producto> findByCategoriaIdAndActivoTrue(Long categoriaId);
}

