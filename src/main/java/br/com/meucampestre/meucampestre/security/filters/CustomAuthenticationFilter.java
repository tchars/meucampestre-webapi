package br.com.meucampestre.meucampestre.security.filters;

import br.com.meucampestre.meucampestre.apimodels.autenticacao.AutenticarRequest;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Value("${app.jwt-secret}")
    private String segredo;

    @Value("${app.jwt-expiration-milliseconds}")
    private String expiration;

    private final AuthenticationManager _authenticationManager;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager)
    {
        _authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException
    {
//        String documento = request.getParameter("documento");
//        String senha = request.getParameter("senha");
//
//        log.info("Autenticando usuário : {} - senha: {}", documento, senha);
//
//        UsernamePasswordAuthenticationToken authenticationToken =
//                new UsernamePasswordAuthenticationToken(documento, senha);
//
//        return _authenticationManager.authenticate(authenticationToken);

        if (!request.getMethod().equals("POST"))
        {
            throw new AuthenticationServiceException("Método não suportado: " + request.getMethod());
        }

        AutenticarRequest loginRequest = getLoginRequest(request);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequest.getDocumento(),
                        loginRequest.getSenha());

        setDetails(request, authenticationToken);

        return _authenticationManager.authenticate(authenticationToken);
    }

    private AutenticarRequest getLoginRequest(HttpServletRequest request)
    {
        BufferedReader reader = null;
        AutenticarRequest loginRequest = null;

        try
        {
            reader = request.getReader();
            Gson gson = new Gson();
            loginRequest = gson.fromJson(reader, AutenticarRequest.class);
        }
        catch (IOException ex)
        {
            Logger.getLogger(AuthenticationFilter.class.getName()).log(Level.SEVERE, null, ex);
        }

        finally
        {
            try
            {
                reader.close();
            }
            catch (IOException ex)
            {
                Logger.getLogger(AuthenticationFilter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (loginRequest == null)
        {
            loginRequest = new AutenticarRequest();
        }

        return loginRequest;
    }


    // responsável por gerar o token
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authentication)
            throws IOException
    {
        User user = (User)authentication.getPrincipal();

        // TODO: Alterar para variável de ambiente
        Algorithm algorithm = Algorithm.HMAC256("segredo".getBytes());

        String access_token = JWT.create()
                .withSubject(user.getUsername())
                // TODO: Alterar para variável de ambiente
                .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles",
                        user.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);

        String refresh_token = JWT.create()
                .withSubject(user.getUsername())
                // TODO: Alterar para variável de ambiente
                .withExpiresAt(new Date(System.currentTimeMillis() + 30 * 60 * 1000))
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithm);

        // Para enviar no header
//        response.setHeader("access_token", access_token);
//        response.setHeader("refresh_token", refresh_token);

        // Para enviar no body
        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", access_token);
        tokens.put("refresh_token", refresh_token);

        response.setContentType(APPLICATION_JSON_VALUE);

        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }
}
