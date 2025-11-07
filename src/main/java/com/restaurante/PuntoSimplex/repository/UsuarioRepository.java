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
            // ** IMPORTANTE: ELIMINAR TODA LA LÓGICA DEL NOMBRE/LIKE DE AQUÍ **
            "(:rolId IS NULL OR u.rol.id = :rolId) AND " +
            "(:activo IS NULL OR u.activo = :activo)")
    List<Usuario> findByFiltrosPersonalizados(
          //  @Param("nombre") String nombre,
            @Param("rolId") Long rolId,
            @Param("activo") Boolean activo
    );
}
// Filtro por Nombre: busca en primer/segundo nombre/apellido. Si :nombre es NULL, siempre es TRUE.

