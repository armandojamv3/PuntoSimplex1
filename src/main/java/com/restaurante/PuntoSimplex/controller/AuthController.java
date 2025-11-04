package com.restaurante.PuntoSimplex.controller;



import com.restaurante.PuntoSimplex.Model.Usuario;
import com.restaurante.PuntoSimplex.config.JwtSecretProvider;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;




import java.util.Date;


@RestController
@RequestMapping("/api/auth")
public class AuthController {



    @Value("${jwt.expiration}")
    private long expirationTime;

    // ARREGLO: @Value debe aplicarse a un campo de la clase, no a una variable local.


    private final AuthenticationManager authenticationManager;
    private final JwtSecretProvider jwtSecretProvider;

    public AuthController(AuthenticationManager authenticationManager, JwtSecretProvider jwtSecretProvider) {
        this.authenticationManager = authenticationManager;
        this.jwtSecretProvider = jwtSecretProvider;
    }

    @PostMapping("/login")
    public String login(@RequestBody Usuario usuario) {
        // Autenticación con el AuthenticationManager
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(usuario.getUsuario(), usuario.getPassword())
        );

        long now = System.currentTimeMillis();

        // Generación del JWT
        String token = Jwts.builder()
                .setSubject(usuario.getUsuario())
                .setIssuedAt(new Date(now))
                // Ahora se usa el campo de clase 'expirationTime' inyectado correctamente.
                .setExpiration(new Date(now + expirationTime))
                .signWith(jwtSecretProvider.getSigningKey(), SignatureAlgorithm.HS256)
                .compact();

        return "Bearer " + token;
    }
}




//
//        // Autenticación con el AuthenticationManager
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(usuario.getUsuario(), usuario.getPassword())
//        );
//
//        // Generación del JWT
//        String token = Jwts.builder()
//                .setSubject(usuario.getUsuario())  // El "usuario" es el nombre de usuario del admin
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + 864_000_00))  // Expiración de 1 día
//                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
//                .compact();
//
//        return "Bearer " + token;  // Devuelves el token JWT en la respuesta
//       // return "String";
//    }
//
//}
