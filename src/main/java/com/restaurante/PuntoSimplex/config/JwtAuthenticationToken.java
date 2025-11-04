package com.restaurante.PuntoSimplex.config;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import io.jsonwebtoken.Claims;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {



    private final Claims claims;

    public JwtAuthenticationToken(Claims claims) {
        super(null);  // Pasamos null ya que las autoridades (roles) no se conocen aún
        this.claims = claims;
        setAuthenticated(true);  // Marca el token como autenticado
    }

    public Claims getClaims() {
        return claims;
    }

    @Override
    public Object getCredentials() {
        return null;  // El token ya no es necesario, solo las claims
    }

    @Override
    public Object getPrincipal() {
        return claims.getSubject();  // El nombre de usuario está contenido en las claims
    }
}
