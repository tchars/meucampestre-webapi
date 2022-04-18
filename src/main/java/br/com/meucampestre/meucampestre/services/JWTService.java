package br.com.meucampestre.meucampestre.services;

import br.com.meucampestre.meucampestre.domain.models.Papel;
import br.com.meucampestre.meucampestre.domain.models.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
@Service
public class JWTService {

    private final UsuarioService _usuarioService;

    @Value("${app.jwt-secret}")
    private String secret;

    @Value("${app.jwt-expiration-milliseconds}")
    private String expiration;

    // TODO: Generalizar service
    public Boolean gerarRefreshToken(HttpServletRequest request,
                      HttpServletResponse response, String authorizationHeader) throws IOException
    {
        try
        {
            String refresh_token = authorizationHeader.substring("Bearer ".length());
            Algorithm algorithm = Algorithm.HMAC256("segredo".getBytes());
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(refresh_token);

            String documento = decodedJWT.getSubject();
            Usuario user = _usuarioService.buscarUsuarioPeloDocumento(documento);

            String access_token = JWT.create()
                    .withSubject(user.getDocumento())
                    // TODO: Alterar para variável de ambiente
                    .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                    .withIssuer(request.getRequestURL().toString())
                    .withClaim("roles",
                            user.getPapeis()
                                    .stream()
                                    .map(Papel::getNome).collect(Collectors.toList()))
                    .sign(algorithm);

            // Para enviar no body
            Map<String, String> tokens = new HashMap<>();
            tokens.put("access_token", access_token);
            tokens.put("refresh_token", refresh_token);

            response.setContentType(APPLICATION_JSON_VALUE);

            new ObjectMapper().writeValue(response.getOutputStream(), tokens);

            return true;
        }
        catch (Exception e)
        {
            response.setHeader("error", e.getMessage());
            response.setStatus(FORBIDDEN.value());

            Map<String, String> error = new HashMap<>();
            error.put("error_message", e.getMessage());

            response.setContentType(APPLICATION_JSON_VALUE);

            new ObjectMapper().writeValue(response.getOutputStream(), error);

            return false;
        }
    }

    public String obterDocumentoPeloToken(String token) {

        Algorithm algorithm = Algorithm.HMAC256("segredo".getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);

        return decodedJWT.getSubject();
    }
}
