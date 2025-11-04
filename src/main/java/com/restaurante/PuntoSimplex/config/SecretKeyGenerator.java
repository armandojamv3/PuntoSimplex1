package com.restaurante.PuntoSimplex.config;

import java.security.SecureRandom;
import java.util.Base64;

public class SecretKeyGenerator {
    // Un tamaño de clave seguro para HMAC-SHA256 es de 256 bits (32 bytes).
    private static final int KEY_SIZE_BYTES = 32;

    public static void main(String[] args) {
        // 1. Generar un array de bytes aleatorio criptográficamente fuerte
        SecureRandom secureRandom = new SecureRandom();
        byte[] keyBytes = new byte[KEY_SIZE_BYTES];
        secureRandom.nextBytes(keyBytes);

        // 2. Codificar el array de bytes usando la codificación Base64 estándar
        // Nota: Usamos el codificador básico, no el 'URL-safe'.
        String base64Key = Base64.getEncoder().encodeToString(keyBytes);

        System.out.println("----------------------------------------------------------------");
        System.out.println("SOLUCIÓN CRÍTICA: Nueva Clave Secreta JWT Generada (Base64)");
        System.out.println("----------------------------------------------------------------");
        System.out.println("Copia la siguiente clave y pégala en tu archivo 'application.properties' o 'application.yml':");
        System.out.println();
        System.out.println("jwt.secret=" + base64Key);
        System.out.println();
        System.out.println("----------------------------------------------------------------");
        System.out.println("Reinicia tu aplicación Spring Boot después de actualizar el archivo.");
        System.out.println("----------------------------------------------------------------");
    }
}

