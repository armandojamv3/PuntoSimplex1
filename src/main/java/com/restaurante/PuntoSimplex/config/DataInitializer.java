    package com.restaurante.PuntoSimplex.config;



    import com.restaurante.PuntoSimplex.Model.Rol;
    import com.restaurante.PuntoSimplex.Model.Usuario;
    import com.restaurante.PuntoSimplex.repository.RolReposirory;
    import com.restaurante.PuntoSimplex.repository.UsuarioRepository;
    import org.springframework.boot.CommandLineRunner;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

    @Configuration
    public class DataInitializer {

        @Bean
        CommandLineRunner initRoles(RolReposirory rolReposirory) {
            return args -> {
                createRoleIfNotExists(rolReposirory, 1L, "ADMINISTRADOR");
                createRoleIfNotExists(rolReposirory, 2L, "CAJERO");
                createRoleIfNotExists(rolReposirory, 3L, "MESERO");
                createRoleIfNotExists(rolReposirory, 4L, "COCINERO");
            };
        }

        private void createRoleIfNotExists(RolReposirory repo, Long id, String descripcion) {
            if (!repo.existsById(id)) {
                repo.save(new Rol(id, descripcion));
                System.out.println("✅ Rol creado: " + descripcion);
            } else {
                System.out.println("ℹ️ Rol ya existente: " + descripcion);
            }
        }

        // Método para crear un usuario ADMIN si no existe
        @Bean
        CommandLineRunner initUsuarios(UsuarioRepository usuarioRepository, RolReposirory rolReposirory, BCryptPasswordEncoder passwordEncoder) {
            return args -> {
                // Obtiene el rol de ADMINISTRADOR
                Rol adminRole = rolReposirory.findByDescripcion("ADMINISTRADOR");

                // Si no existe el usuario "admin" en la base de datos, lo crea
                if (usuarioRepository.findByUsuario("admin").isEmpty()) {
                    Usuario admin = new Usuario();
                    admin.setUsuario("admin");
                    admin.setPassword(passwordEncoder.encode("admin")); // Encriptar la contraseña
                    admin.setPrimerNombre("Jose");
                    admin.setSegundoNombre("Armando");
                    admin.setPrimerApellido("Martinez");
                    admin.setSegundoApellido("Villamizar");
                    admin.setActivo(true);
                    admin.setRol(adminRole); // Asignar el rol de ADMINISTRADOR al usuario
                    usuarioRepository.save(admin); // Guardar el usuario en la base de datos
                    System.out.println("✅ Usuario Admin creado exitosamente");
                }
            };
        }
    }