package br.com.meucampestre.meucampestre.controllers;

import br.com.meucampestre.meucampestre.applications.AutenticacaoApplication;
import br.com.meucampestre.meucampestre.domain.constants.Rotas;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping(Rotas.URL_PREFIX_V1 + "/autenticacao")
public class AutenticacaoController {

    private final AutenticacaoApplication _autenticacaoApplication;

    // Renovar token
    @GetMapping("/renovarToken")
    public ResponseEntity<?> refreshToken(HttpServletRequest request,
                                       HttpServletResponse response) throws IOException
    {
        Boolean tokenRenovado = _autenticacaoApplication.renovarToken(request, response);

        if (tokenRenovado)
        {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.badRequest().body("Token ausente");
    }
}
