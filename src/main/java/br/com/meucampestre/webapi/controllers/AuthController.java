package br.com.meucampestre.webapi.controllers;

import br.com.meucampestre.webapi.dto.autenticacao.CriarNovoUsuarioDTO;
import br.com.meucampestre.webapi.dto.autenticacao.LoginDTO;
import br.com.meucampestre.webapi.dto.usuario.UsuarioDTO;
import br.com.meucampestre.webapi.entities.Role;
import br.com.meucampestre.webapi.entities.Usuario;
import br.com.meucampestre.webapi.repositories.IRoleRepository;
import br.com.meucampestre.webapi.repositories.IUsuarioRepository;
import br.com.meucampestre.webapi.dto.autenticacao.JWTAuthResponseDTO;
import br.com.meucampestre.webapi.services.interfaces.IAutenticacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final IAutenticacaoService _autenticacaoService;

    @Autowired
    public AuthController(IAutenticacaoService autenticacaoService) {
        _autenticacaoService = autenticacaoService;
    }

    @PostMapping("/iniciarSessao")
    public ResponseEntity<JWTAuthResponseDTO> authenticateUser(@RequestBody LoginDTO loginDTO)
    {
        return ResponseEntity.ok(_autenticacaoService.autenticarUsuario(loginDTO));
    }

    @PostMapping("/registrar")
    public ResponseEntity<CriarNovoUsuarioDTO> registrarUsuario(@RequestBody UsuarioDTO registroDTO)
    {
        return new ResponseEntity<>(_autenticacaoService.registrarNovoUsuario(registroDTO), HttpStatus.CREATED);
    }
}
