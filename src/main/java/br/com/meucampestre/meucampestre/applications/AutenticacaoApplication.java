package br.com.meucampestre.meucampestre.applications;

import br.com.meucampestre.meucampestre.services.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Service
@RequiredArgsConstructor
public class AutenticacaoApplication {

    private final JWTService _jwtService;

    // ??
    public void autenticarUsuario() {

    }

    // Renovar token
    public Boolean renovarToken(HttpServletRequest request,
                                HttpServletResponse response) throws IOException {

        String authorizationHeader = request.getHeader(AUTHORIZATION);

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer "))
        {
            return _jwtService.gerarRefreshToken(request, response, authorizationHeader);
        }

        return false;
    }
}
