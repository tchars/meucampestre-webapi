package br.com.meucampestre.meucampestre.controllers;

import br.com.meucampestre.meucampestre.domain.constants.Rotas;
import br.com.meucampestre.meucampestre.domain.models.Papel;
import br.com.meucampestre.meucampestre.domain.models.Usuario;
import br.com.meucampestre.meucampestre.services.UsuarioService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(Rotas.URL_PREFIX_V1 + "/usuarios")
public class UsuarioController {

    private final UsuarioService _usuarioService;

    @GetMapping
    public ResponseEntity<List<Usuario>> buscarTodosUsuarios() {
        return ResponseEntity.ok().body(_usuarioService.buscarUsuarios());
    }

    @PostMapping("/salvar")
    public ResponseEntity<Usuario> salvarUsuario(@RequestBody Usuario usuario) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/usuarios/salvar").toUriString());
        return ResponseEntity.created(uri).body(_usuarioService.salvarUsuario(usuario));
    }

    @PostMapping("/papeis/salvar")
    public ResponseEntity<Papel> salvarPapel(@RequestBody Papel papel) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/papeis/salvar").toUriString());
        return ResponseEntity.created(uri).body(_usuarioService.salvarPapel(papel));
    }

    @PostMapping("/adicionarAoUsuario")
    public ResponseEntity<?> adicionarPapelAoUsuario(@RequestBody PapelAoUsuarioRequest request) {

        _usuarioService.adicionarPapelAoUsuario(request.getDocumento(), request.getPapel());

        return ResponseEntity
                .ok()
                .build();
    }
}

@Data
class PapelAoUsuarioRequest {
    private String documento;
    private String papel;
}
