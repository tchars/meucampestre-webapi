package br.com.meucampestre.webapi.services;

import br.com.meucampestre.webapi.entities.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class JwtService {

    @Value("${app.jwt-secret}")
    private String secret;

    @Value("${app.jwt-expiration-milliseconds}")
    private String expiration;

    public String generateToken(Usuario usuario) {

        long expString = Long.valueOf(expiration);
        LocalDateTime dataHoraExpiracao = LocalDateTime.now().plusMinutes(expString);
        Instant instant = dataHoraExpiracao.atZone(ZoneId.systemDefault()).toInstant();
        Date data = Date.from(instant);

        return Jwts
                .builder()
                .claim("name", usuario.getNome())
                .setSubject(usuario.getEmail())
                .setExpiration(data)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    private Claims getClaims(String token) throws ExpiredJwtException {
        return Jwts
                .parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = getClaims(token);
            Date dataExpiracao = claims.getExpiration();
            LocalDateTime data =
                    dataExpiracao.toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDateTime();

            return !LocalDateTime.now().isAfter(data);
        }
        catch (Exception e) {
            return false;
        }
    }

    public String getUsernameFromToken(String token) throws ExpiredJwtException {
        return getClaims(token).getSubject();
    }
}
