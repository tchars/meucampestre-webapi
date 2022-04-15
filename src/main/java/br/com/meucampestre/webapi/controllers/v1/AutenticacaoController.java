package br.com.meucampestre.webapi.controllers.v1;

import br.com.meucampestre.webapi.dto.autenticacao.LoginDTO;
import br.com.meucampestre.webapi.dto.autenticacao.v1.TokenDTO;
import br.com.meucampestre.webapi.dto.autenticacao.v1.ValidateTokenDTO;
import br.com.meucampestre.webapi.services.AuthService;
import br.com.meucampestre.webapi.utils.Constantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(Constantes.ROUTES_PREFIX_V1 + "/session")
public class AutenticacaoController {

    private final AuthService _authService;

    @Autowired
    public AutenticacaoController(AuthService authService) {
        _authService = authService;
    }

    @PostMapping("/auth")
    public TokenDTO authenticate(@RequestBody LoginDTO loginDTO)
    {
        // TODO: remover responsabilidade do controller
        try {
            //LogtailExternalService.createLog("Controller: Criando JWT: " + loginDTO.getEmail());

            System.out.println("Controller: Criando JWT: " + loginDTO.getEmail());

            return _authService.authenticate(loginDTO);
        }
        catch (Exception e) {
            //LogtailExternalService.createLog("Controller: Algo deu errado ao criar JWT: " + loginDTO.getEmail());

            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PostMapping("/validate")
    public ValidateTokenDTO validate(@RequestBody ValidateTokenDTO validateTokenDTO) {
        try {
            //LogtailExternalService.createLog("Controller: Validando JWT: " + validateTokenDTO.getToken());

            System.out.println("Controller: Validando JWT: " + validateTokenDTO.getToken());

            return _authService.validateToken(validateTokenDTO);
        }
        // TODO: NEM LEMBRO, TRATAR ISSO
        catch (Exception e) {
            //LogtailExternalService.createLog("Controller: Token inválido: " + validateTokenDTO.getToken());

            System.out.println("Controller: Token inválido: " + validateTokenDTO.getToken());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}
