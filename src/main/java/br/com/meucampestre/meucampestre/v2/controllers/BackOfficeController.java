package br.com.meucampestre.meucampestre.v2.controllers;

import br.com.meucampestre.meucampestre.domain.constants.Rotas;
import br.com.meucampestre.meucampestre.domain.models.Condominio;
import br.com.meucampestre.meucampestre.domain.models.Unidade;
import br.com.meucampestre.meucampestre.domain.models.Usuario;
import br.com.meucampestre.meucampestre.v2.apiModels.requests.AdicionarPapelAoUsuarioRequest;
import br.com.meucampestre.meucampestre.v2.services.BackOfficeServiceV2;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(Rotas.URL_PREFIX_V2 + "/backoffice")
public class BackOfficeController {

    private final BackOfficeServiceV2 backOfficeServiceV2;

    // ----------------------------
    // CONTEXTO: CONDOMINIO
    // ----------------------------
    @GetMapping("/condominios")
    public ResponseEntity<List<Condominio>> buscarTodosCondominios()
    {
        return ResponseEntity.ok().body(backOfficeServiceV2.buscarTodosCondominios());
    }

    @GetMapping("/condominios/{idCondominio}")
    public ResponseEntity<Condominio> buscarCondominioPeloId(@PathVariable long idCondominio)
    {
        return ResponseEntity.ok().body(backOfficeServiceV2.buscarCondominioPeloId(idCondominio));
    }

    @PostMapping("/condominios")
    public ResponseEntity<Condominio> salvarCondominio(@RequestBody Condominio request)
    {
        return ResponseEntity.ok().body(backOfficeServiceV2.salvarCondominio(request));
    }

    @PutMapping("/condominios")
    public ResponseEntity<Condominio> atualizarCondominio(@RequestBody Condominio request)
    {
        return ResponseEntity.ok().body(backOfficeServiceV2.atualizarCondominio(request));
    }

    @DeleteMapping("/condominios/{idCondominio}")
    public ResponseEntity<Condominio> deletarCondominio(@PathVariable long idCondominio)
    {
        return ResponseEntity.ok().body(backOfficeServiceV2.deletarCondominio(idCondominio));
    }

    // ----------------------------
    // CONTEXTO: UNIDADES
    // ----------------------------
    @GetMapping("/condominios/{idCondominio}/unidades")
    public ResponseEntity<List<Unidade>> buscarUnidadesDeUmCondominio(@PathVariable long idCondominio)
    {
        return ResponseEntity.ok().body(backOfficeServiceV2.buscarTodasUnidadesDeUmCondominio(idCondominio));
    }

    @PostMapping("/condominios/{idCondominio}/unidades")
    public ResponseEntity<Unidade> salvarUnidadeAoCondominio(@PathVariable long idCondominio,
                                                             @RequestBody Unidade request)
    {
        return ResponseEntity.ok().body(backOfficeServiceV2.salvarUnidade(idCondominio, request));
    }

    // ----------------------------
    // CONTEXTO: USUÁRIO
    // ----------------------------
    @GetMapping("/usuarios")
    public ResponseEntity<List<Usuario>> buscarTodoUsuarios()
    {
        return ResponseEntity.ok().body(backOfficeServiceV2.buscarTodoUsuarios());
    }

    @GetMapping("/usuarios/{idUsuario}")
    public ResponseEntity<Usuario> buscarUsuarioPeloId(@PathVariable long idUsuario)
    {
        return ResponseEntity.ok().body(backOfficeServiceV2.buscarUsuarioPeloId(idUsuario));
    }

    @PostMapping("/usuarios")
    public ResponseEntity<Usuario> salvarUsuario(@RequestBody Usuario request)
    {
        return ResponseEntity.ok().body(backOfficeServiceV2.salvarUsuario(request));
    }

    @PutMapping("/usuarios")
    public ResponseEntity<Usuario> atualizarUsuario(@RequestBody Usuario request)
    {
        return ResponseEntity.ok().body(backOfficeServiceV2.atualizarDadosUsuario(request));
    }

    @DeleteMapping("/usuarios/{idUsuario}")
    public ResponseEntity<Usuario> deletarUsuario(@PathVariable long idUsuario)
    {
        return ResponseEntity.accepted().body(backOfficeServiceV2.deletarUsuario(idUsuario));
    }

    // ----------------------------
    // CONTEXTO: PAPEIS
    // ----------------------------
    @PostMapping("/papeis")
    public ResponseEntity<?> adicionarPapelAoUsuario(@RequestBody AdicionarPapelAoUsuarioRequest request)
    {
        return ResponseEntity.ok().body(backOfficeServiceV2
                .adicionarPapelAoUsuario(
                        request.getIdUsuario(),
                        request.getNomePapel(),
                        request.getIdCondominio(),
                        request.isTipoEspecial())
        );
    }

}
