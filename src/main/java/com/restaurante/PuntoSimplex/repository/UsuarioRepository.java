package com.restaurante.PuntoSimplex.repository;

import com.restaurante.PuntoSimplex.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
//Acceso y persistencia directos a la Base de Datos (DB).
@Repository
// Hereda todos los métodos CRUD de JPA
//es la capa que habla directamente con la base de datos.
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Buscar usuario por nombre de usuario
    Optional<Usuario> findByUsuario(String usuario);

    /**
     * Consulta JPQL personalizada para filtrar usuarios por parámetros opcionales.
     * Si un parámetro es NULL, esa condición se ignora, permitiendo la flexibilidad.
     * La búsqueda por nombre es insensible a mayúsculas/minúsculas y busca en primer y segundo nombre/apellido.
     */
    @Query("SELECT u FROM Usuario u WHERE " +
            // Filtro por Nombre: busca en primer/segundo nombre/apellido. Si :nombre es NULL, siempre es TRUE.
            "(:nombre IS NULL OR " +
            "LOWER(u.primerNombre) LIKE LOWER(CONCAT('%', :nombre, '%')) OR " +
            "LOWER(u.segundoNombre) LIKE LOWER(CONCAT('%', :nombre, '%')) OR " +
            "LOWER(u.primerApellido) LIKE LOWER(CONCAT('%', :nombre, '%')) OR " +
            "LOWER(u.segundoApellido) LIKE LOWER(CONCAT('%', :nombre, '%'))) AND " +
            // Filtro por Rol ID: Si :rolId es NULL, siempre es TRUE.
            "(:rolId IS NULL OR u.rol.id = :rolId) AND " +
            // Filtro por Estado Activo: Si :activo es NULL, siempre es TRUE.
            "(:activo IS NULL OR u.activo = :activo)")
    List<Usuario> findByFiltrosPersonalizados(
            @Param("nombre") String nombre,
            @Param("rolId") Long rolId,
            @Param("activo") Boolean activo
    );
}

