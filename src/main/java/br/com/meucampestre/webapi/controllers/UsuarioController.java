package br.com.meucampestre.webapi.controllers;

import br.com.meucampestre.webapi.dto.usuario.UsuarioDTO;
import br.com.meucampestre.webapi.services.interfaces.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final IUsuarioService _usuarioService;

    @Autowired
    public UsuarioController(IUsuarioService usuarioService) {
        _usuarioService = usuarioService;
    }

    // Crio um usuário
    @PostMapping
    public ResponseEntity<UsuarioDTO> criarCondominio(@RequestBody UsuarioDTO usuarioDTO) {
        return new ResponseEntity<>(_usuarioService.criarUsuario(usuarioDTO), HttpStatus.CREATED);
    }
}
