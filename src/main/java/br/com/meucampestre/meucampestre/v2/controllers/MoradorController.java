package br.com.meucampestre.meucampestre.v2.controllers;

import br.com.meucampestre.meucampestre.apimodels.condominio.partials.MoradorDoCondominio;
import br.com.meucampestre.meucampestre.apimodels.usuarios.CriarUsuarioRequestV2;
import br.com.meucampestre.meucampestre.domain.constants.Rotas;
import br.com.meucampestre.meucampestre.domain.models.Usuario;
import br.com.meucampestre.meucampestre.v2.services.MoradorServiceV2;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequiredArgsConstructor
@RequestMapping(Rotas.URL_PREFIX_V2 + "/usuarios")
public class MoradorController {

    private final MoradorServiceV2 moradorServiceV2;

    @GetMapping("/{idCondominio}/usuario")
    public ResponseEntity<ArrayList<MoradorDoCondominio>> buscarTodosUsuariosDeUmCondominio(@PathVariable long idCondominio)
    {
        return ResponseEntity.ok().body(moradorServiceV2.buscarTodosUsuariosDeUmCondominio(idCondominio));
    }

    @GetMapping("/{idCondominio}/usuario/{documento}")
    public ResponseEntity<Usuario> buscarUmUsuarioEmUmCondominio(@PathVariable long idCondominio, @PathVariable String documento)
    {
        return ResponseEntity.ok().body(moradorServiceV2.buscarUmUsuarioEmUmCondominio(idCondominio, documento));
    }

    @PostMapping("/{idCondominio}/usuario")
    public ResponseEntity<Usuario> cadastrarUsuarioEmUmCondominio(@PathVariable long idCondominio, @RequestBody CriarUsuarioRequestV2 request)
    {
        return ResponseEntity.ok().body(moradorServiceV2.criarUsuario(idCondominio, request));
    }

    @PutMapping("/{idCondominio}/usuario/{documento}")
    public ResponseEntity<Usuario> cadastrarUsuarioEmUmCondominio(@PathVariable long idCondominio, @RequestBody CriarUsuarioRequestV2 request, @PathVariable String documento)
    {
        return ResponseEntity.ok().body(moradorServiceV2.atualizarDadosUsuario(idCondominio, request, documento));
    }

    @DeleteMapping("/{idCondominio}/usuario/{documento}")
    public ResponseEntity<?> removerPermissoesUsuario(@PathVariable long idCondominio, @PathVariable String documento)
    {
        moradorServiceV2.removerPermissoesUsuario(idCondominio, documento);
        return ResponseEntity.ok().build();
    }

}
