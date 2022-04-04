package br.com.meucampestre.webapi.security;

import java.util.Date;

import br.com.meucampestre.webapi.exceptions.AppException;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class JWTTokenProvider {

    @Value("${app.jwt-secret}")
    private String jwtSecret;

    @Value("${app.jwt-expiration-milliseconds}")
    private int jwtExpirationInMs;

    public String gerarToken(Authentication authentication)
    {
        String username = authentication.getName();
        Date dataInicial = new Date();
        Date dataExpiracao = new Date(dataInicial.getTime() + jwtExpirationInMs);

        String token = Jwts.builder().setSubject(username)
                                     .setIssuedAt(new Date()).setExpiration(dataExpiracao)
                                     .signWith(SignatureAlgorithm.HS512, jwtSecret)
                                     .compact();

        return token;
    }

    public String obterEmailPeloJWT(String token) {

        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();

        return claims.getSubject();
    }

    public boolean validarToken(String token)
    {
        try
        {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);

            return true;
        }
        catch (SignatureException ex) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Assinatura inválida");
        }
        catch (MalformedJwtException ex) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Token inválido");
        }
        catch (ExpiredJwtException ex) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Token expirado");
        }
        catch (UnsupportedJwtException ex) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Token incompatível");
        }
        catch (IllegalArgumentException ex) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Não possui acesso");
        }
    }
}
