package br.com.meucampestre.meucampestre.v2.controllers;

import br.com.meucampestre.meucampestre.domain.constants.Rotas;
import br.com.meucampestre.meucampestre.domain.models.Unidade;
import br.com.meucampestre.meucampestre.domain.models.UnidadeUsuario;
import br.com.meucampestre.meucampestre.v2.apiModels.requests.AdicionarUsuarioAUnidadeRequest;
import br.com.meucampestre.meucampestre.v2.services.UnidadeServiceV2;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(Rotas.URL_PREFIX_V2 + "/unidades")
public class UnidadeController {

    private final UnidadeServiceV2 unidadeServiceV2;

    @GetMapping("/condominios/{idCondominio}")
    public ResponseEntity<List<Unidade>> buscarUnidadesDeUmCondominio(@PathVariable long idCondominio)
    {
        return ResponseEntity.ok().body(unidadeServiceV2.buscarTodasUnidadesDeUmCondominio(idCondominio));
    }

    @GetMapping("/condominios/{idCondominio}/unidades/{idUnidade}")
    public ResponseEntity<Unidade> buscarUnidadeDeUmCondominio(@PathVariable long idCondominio,
                                                               @PathVariable long idUnidade)
    {
        return ResponseEntity.ok().body(unidadeServiceV2.buscarUnidadeDeUmCondominio(idCondominio, idUnidade));
    }

    @PostMapping("/condominios/{idCondominio}/unidades")
    public ResponseEntity<Unidade> salvarUnidadeAoCondominio(@PathVariable long idCondominio,
                                                             @RequestBody Unidade request)
    {
        return ResponseEntity.ok().body(unidadeServiceV2.salvarUnidade(idCondominio, request));
    }

    @PostMapping("/condominios/{idCondominio}/unidades/{idUnidade}/usuarios")
    public ResponseEntity<UnidadeUsuario> adicionarUsuarioAUnidade(@PathVariable long idCondominio,
                                                                   @PathVariable long idUnidade,
                                                                   @RequestBody
                                                                           AdicionarUsuarioAUnidadeRequest documento)
    {
        return ResponseEntity
                .ok()
                .body(unidadeServiceV2
                        .adicionarUsuarioAUnidade(idCondominio, idUnidade, documento.getDocumento()));
    }

    @PutMapping("/condominios/{idCondominio}/unidades/{idUnidade}")
    public ResponseEntity<Unidade> atualizarDadosUnidade(@PathVariable long idCondominio,
                                                         @PathVariable long idUnidade,
                                                         @RequestBody Unidade unidade)
    {
        return ResponseEntity.ok().body(unidadeServiceV2
                .atualizarDadosUnidade(idCondominio, idUnidade, unidade)
        );
    }

    @DeleteMapping("/condominios/{idCondominio}/unidades/{idUnidade}")
    public ResponseEntity<?> excluirUnidade(@PathVariable long idCondominio,
                                            @PathVariable long idUnidade)
    {
        unidadeServiceV2.excluirUnidade(idCondominio, idUnidade);

        return ResponseEntity.ok().build();
    }
}
