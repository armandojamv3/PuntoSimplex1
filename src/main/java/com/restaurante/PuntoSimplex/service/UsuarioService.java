package com.restaurante.PuntoSimplex.service;

import com.restaurante.PuntoSimplex.Dto.CrearUsuarioRequestDTO;
import com.restaurante.PuntoSimplex.Dto.UsuarioDto;
import com.restaurante.PuntoSimplex.Model.Rol;
import com.restaurante.PuntoSimplex.Model.Usuario;
import com.restaurante.PuntoSimplex.repository.RolReposirory;
import com.restaurante.PuntoSimplex.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service // Define esta clase como servicio, parte de la capa de negocio.
@RequiredArgsConstructor // Genera automáticamente un constru con los atribs final (en este caso, usuarioRepository).

public class UsuarioService {


    //@Autowired

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RolReposirory rolReposirory; // para verificar si el rol_id que te envian es valido


    /**
     * Listar todos los usuarios con filtros opcionales.
     * Llama al método avanzado del repositorio, que maneja los NULLs para ignorar filtros.
     * @paramnombre Nombre o apellido para buscar (opcional).
     * @paramrolId ID del rol para filtrar (opcional).
     * @paramactivo Estado activo del usuario para filtrar (opcional).
     * @return Lista de usuarios que cumplen con los criterios.
     */
    // Listar todos los usuarios
    public List<Usuario> listarUsuarios( String nombre, Long rolId, Boolean activo) {
        // La lógica de la consulta con manejo de NULLs ya está en el Repository
        return usuarioRepository.findByFiltrosPersonalizados(nombre,rolId,activo);
    }

    //  Buscar usuario por ID
    public Optional<Usuario> obtenerUsuarioPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    @Transactional
    //  Crear un nuevo usuario
    public Usuario crearUsuarioDesdeDto(CrearUsuarioRequestDTO request) {

        // Verifica si el nombre de usuario ya existe en la DB
        //findByUsuario buscar en la bd un registro
        //.isPresent() que devuelve true si hay un valor dentro (es decir, si el usuario ya existe),
        // o false si está vacío (no existe ningún usuario con ese nombre).
        // 1. Validación de Negocio: verifica si el nombre de usuario ya existe
        if (usuarioRepository.findByUsuario(request.getUsuario()).isPresent()) {
            throw new IllegalArgumentException("El nombre de usuario '" + request.getUsuario() + "' ya está en uso.");
        }

        // 2. Mapeo DTO a Entidad Usuario
        Usuario usuario = new Usuario();
        usuario.setPrimerNombre(request.getPrimerNombre());
        usuario.setSegundoNombre(request.getSegundoNombre());
        usuario.setPrimerApellido(request.getPrimerApellido());
        usuario.setSegundoApellido(request.getSegundoApellido());
        usuario.setUsuario(request.getUsuario());

        // 3. Asignación de Contraseña y Rol

        // 3.1 Cifrar y asignar la contraseña
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));

        // 3.2 Buscar y validar la existencia del Rol
        Rol rolAsignado = rolReposirory.findById(request.getRolId()) // Usa el rolId del DTO
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + request.getRolId()));
        usuario.setRol(rolAsignado);

        // 3.3 El usuario se crea como activo
        if (usuario.getActivo() == null) {
            usuario.setActivo(true);
        }

        // 4. Guardar y devolver
        return usuarioRepository.save(usuario);
    }

    //  Actualizar usuario existente
    public Usuario actualizarUsuario(Long id, Usuario nuevosDatos) {
        return usuarioRepository.findById(id).map(usuario -> {
            usuario.setPrimerNombre(nuevosDatos.getPrimerNombre());
            usuario.setSegundoNombre(nuevosDatos.getSegundoNombre());
            usuario.setPrimerApellido(nuevosDatos.getPrimerApellido());
            usuario.setSegundoApellido(nuevosDatos.getSegundoApellido());
            usuario.setUsuario(nuevosDatos.getUsuario());
            // IMPORTANTE: Si la contraseña se actualiza, DEBE HASHEARSE(encriptada) aquí también
            if (nuevosDatos.getPassword() != null && !nuevosDatos.getPassword().isEmpty()) {
                usuario.setPassword(passwordEncoder.encode(nuevosDatos.getPassword()));
            }
            usuario.setActivo(nuevosDatos.getActivo());
            usuario.setRol(nuevosDatos.getRol());
            return usuarioRepository.save(usuario);
        }).orElseThrow(() -> new RuntimeException(" Usuario no encontrado con ID: " + id)); //.orElseThrow(...) Si el valor existe devuelve Si no existe, lanza una excepción
    }

    //  Eliminar usuario
    public void eliminarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("️ No se puede eliminar: usuario no encontrado con ID: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    //  Buscar por nombre de usuario
    public Optional<Usuario> buscarPorNombreUsuario(String nombreUsuario) {
        return usuarioRepository.findByUsuario(nombreUsuario);
    }
}