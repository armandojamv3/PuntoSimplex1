package com.restaurante.PuntoSimplex.config;


import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
public class JwtSecretProvider {

    // Se inyecta el valor de la clave secreta codificada en Base64 desde application.properties
    @Value("${jwt.secret}")
    private String jwtSecretBase64;

    private SecretKey key;

    @PostConstruct
    public void init() {
        try {
            // Decodificar la cadena Base64 inyectada en un array de bytes de 256 bits
            byte[] keyBytes = Decoders.BASE64.decode(jwtSecretBase64);
            // Crear la SecretKey segura para el algoritmo HS256
            this.key = Keys.hmacShaKeyFor(keyBytes);

            System.out.println("Clave JWT de" + this.key.getEncoded().length*8 + " 256 bits inicializada correctamente para firma/verificación.");
        } catch (Exception e) {
            // Manejar cualquier error que ocurra si la clave no es una Base64 válida o no es del tamaño adecuado.
            System.err.println("ERROR CRÍTICO: No se pudo inicializar la clave JWT. Verifique que 'jwt.secret' en application.properties sea la clave Base64 Larga Generada.");
            throw new IllegalStateException("Fallo al inicializar la Clave JWT segura.", e);
        }
    }

    /**
     * Devuelve la clave secreta segura de 256 bits (SecretKey).
     */
    public SecretKey getSigningKey() {
        return key;
    }
}
