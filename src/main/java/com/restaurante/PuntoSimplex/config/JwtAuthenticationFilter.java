package com.restaurante.PuntoSimplex.config;




import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.Base64;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    private SecretKey signingKey;

    private final JwtSecretProvider jwtSecretProvider; // Inyectamos el proveedor de clave

    // Ahora inyectamos el proveedor de clave en el constructor.
    public JwtAuthenticationFilter(JwtSecretProvider jwtSecretProvider) {
        this.jwtSecretProvider = jwtSecretProvider;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);


            // Obtenemos la clave segura directamente del proveedor
            SecretKey signingKey = jwtSecretProvider.getSigningKey();

            try {
                // El parser utiliza la SecretKey segura para verificar la firma
                Claims claims = Jwts.parserBuilder() // Usar parserBuilder() en versiones recientes de JJWT
                        .setSigningKey(signingKey)
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

                // Aquí obtenemos el nombre de usuario del token (subject)
                String username = claims.getSubject();

                // Creación y establecimiento del objeto de autenticación
                // Nota: Asumimos que JwtAuthenticationToken existe y está definido.
                // Reemplaza con tu lógica real si es necesario.
                JwtAuthenticationToken authentication = new JwtAuthenticationToken(claims);
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception e) {
                // Manejo de token inválido (incluyendo WeakKeyException o SignatureException)
                System.err.println("Token JWT Inválido o Expirado: " + e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Invalid or expired token: " + e.getMessage() + "\"}");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
