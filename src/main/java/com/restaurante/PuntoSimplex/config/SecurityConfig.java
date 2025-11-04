    package com.restaurante.PuntoSimplex.config;

    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.security.config.Customizer;
    import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
    import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
    import org.springframework.security.config.http.SessionCreationPolicy;
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.security.web.SecurityFilterChain;
    import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
    import org.springframework.security.authentication.AuthenticationManager;
    import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

    import static org.springframework.security.config.Customizer.withDefaults;

    @Configuration
    @EnableWebSecurity
    public class SecurityConfig  {

        private final JwtAuthenticationFilter jwtAuthenticationFilter;

        // Inyección del filtro JWT vía constructor
        public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
            this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        }

        /**
         * Define el bean AuthenticationManager.
         */
        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
            // Obtenemos y exponemos el AuthenticationManager preconfigurado.
            return authenticationConfiguration.getAuthenticationManager();
        }

        /**
         * Define el SecurityFilterChain para configurar las reglas de acceso y el uso de JWT.
         */
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http
                    // 1. Deshabilitar CSRF (común para APIs REST)
                    .csrf(csrf -> csrf.disable())

                    // 2. Configurar el manejo de sesiones como SIN ESTADO (STATELESS)
                    // Esto le indica a Spring que no debe crear ni usar sesiones HTTP para mantener el estado del usuario.
                    .sessionManagement(session -> session
                            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    )

                    // 3. Configurar reglas de autorización
                    .authorizeHttpRequests(auth -> auth
                            // Permitir acceso sin autenticar a las rutas de autenticación
                            .requestMatchers("/login", "/api/auth/**", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                            // Requerir autenticación para cualquier otra petición
                            .anyRequest().authenticated()
                    )

                    // 4. Deshabilitar formularios de login y Basic Auth por defecto
                    .formLogin(form -> form.disable())
                    .httpBasic(basic -> basic.disable())

                    // 5. Añadir el filtro JWT antes del filtro estándar de Spring Security
                    // Esto asegura que el token sea validado antes de que Spring revise la autenticación por nombre/contraseña.
                    .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

            return http.build();
        }

        /**
         * Define el bean PasswordEncoder para encriptar contraseñas.
         */
        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

    }