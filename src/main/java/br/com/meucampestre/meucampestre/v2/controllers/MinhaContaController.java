package br.com.meucampestre.meucampestre.v2.controllers;

import br.com.meucampestre.meucampestre.domain.constants.Rotas;
import br.com.meucampestre.meucampestre.domain.models.Condominio;
import br.com.meucampestre.meucampestre.domain.models.Papel;
import br.com.meucampestre.meucampestre.domain.models.Usuario;
import br.com.meucampestre.meucampestre.v2.apiModels.requests.AtualizarMeusDadosDaContaRequest;
import br.com.meucampestre.meucampestre.v2.domain.models.MinhaConta;
import br.com.meucampestre.meucampestre.v2.services.MinhaContaServiceV2;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(Rotas.URL_PREFIX_V2 + "/minhaConta")
public class MinhaContaController {

    private final MinhaContaServiceV2 minhaContaService;

    @GetMapping
    public ResponseEntity<MinhaConta> buscarMeusDados()
    {
        String usuarioDoToken = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return ResponseEntity.ok().body(minhaContaService.buscarDadosDoUsuario(usuarioDoToken));
    }

    @PutMapping()
    public ResponseEntity<?> atualizarMeusDados(@RequestBody AtualizarMeusDadosDaContaRequest request)
    {
        String usuarioDoToken = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return ResponseEntity.ok().body(minhaContaService.atualizarMeusDados(usuarioDoToken, request));
    }

    @GetMapping("/{idCondominio}")
    public ResponseEntity<List<Papel>> buscarDadosDeUmCondominio(@PathVariable long idCondominio)
    {
        String usuarioDoToken = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return ResponseEntity.ok().body(minhaContaService.buscarRolesAPartirDoCondominio(usuarioDoToken, idCondominio));
    }
}
