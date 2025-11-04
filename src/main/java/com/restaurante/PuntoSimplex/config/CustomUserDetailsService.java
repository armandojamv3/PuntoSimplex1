package com.restaurante.PuntoSimplex.config;



import com.restaurante.PuntoSimplex.Model.Usuario;
import com.restaurante.PuntoSimplex.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class CustomUserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Busca el usuario por su nombre de usuario
        Usuario usuario = usuarioRepository.findByUsuario(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        // Devuelve un objeto UserDetails con la información del usuario y sus roles
        return User.builder()
                .username(usuario.getUsuario())
                .password(usuario.getPassword())  // La contraseña ya está cifrada
                .roles(usuario.getRol().getDescripcion())  // Aquí se obtiene el rol del usuario
                .build();
    }
}
