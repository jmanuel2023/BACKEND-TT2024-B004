/**
 ********************************
 ** Proyecto: Skincanbe        **
 ** Integrantes:               **
 ** Joan Hanzka Manuel Morales **
 ** Angelo Mihaelle Ojeda Gomez**
 ** Israel Rodrigue Juarez     **
 ******************************** 
 * Descripción: Clase de seguridad para la decodificación del token para el restablecimiento de la contraseña.
 */
package com.example.tt2024b004.skincanbe.security;

import java.util.Date;

import com.example.tt2024b004.skincanbe.model.usuario.Usuario;

import static com.example.tt2024b004.skincanbe.security.JwtTokenConfig.*;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

public class JwtUtil {

    // Método para obtener el email desde el token de restablecimiento de contraseña
    public static String getEmailFromPasswordResetToken(String token) {
        try {
            Claims claims = Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token).getPayload();
            String email = claims.getSubject();
            return email;
        } catch (Exception e) {
            // Otros errores posibles durante la decodificación del token
            throw new RuntimeException("Error al procesar el token");
        }
    }

    public static boolean validatePasswordResetToken(String token){
        try {
            System.out.println("Antes del claims");
            Claims claims = Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token).getPayload();
            System.out.println("Despues del claims");
            Date expirationDate = claims.getExpiration();
            if (expirationDate.before(new Date())) {
                System.out.println("El token ha expirado");
                return false; // El token ha expirado
            }
            else{
                return true;
            }
        } catch (JwtException e) {
            // Otros errores posibles durante la decodificación del token
            System.err.println("Error al procesar el token: " + e.getMessage());
            return false;
        }   
    }
    
    public static String generatePasswordResetToken(Usuario usuario) {
        
      String email = usuario.getCorreo();

        Claims claims = Jwts.claims()
                .add("email", email)
        .build();


        return Jwts.builder()
                .subject(email)
                .claims(claims)
                .expiration(new Date(System.currentTimeMillis() + 3600000))
                .issuedAt(new Date())
                .signWith(SECRET_KEY)
                .compact();
    }

}
