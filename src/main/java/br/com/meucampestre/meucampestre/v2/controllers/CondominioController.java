package br.com.meucampestre.meucampestre.v2.controllers;

import br.com.meucampestre.meucampestre.domain.constants.Rotas;
import br.com.meucampestre.meucampestre.domain.models.Condominio;
import br.com.meucampestre.meucampestre.v2.services.CondominioServiceV2;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(Rotas.URL_PREFIX_V2 + "/condominios")
public class CondominioController {

    private final CondominioServiceV2 condominioServiceV2;

    @GetMapping("/{idCondominio}")
    public ResponseEntity<Condominio> buscarCondominioPeloId(@PathVariable long idCondominio)
    {
        return ResponseEntity.ok().body(condominioServiceV2.buscarCondominioPeloId(idCondominio));
    }

    @PutMapping()
    public ResponseEntity<Condominio> atualizarCondominio(@RequestBody Condominio request)
    {
        return ResponseEntity.ok().body(condominioServiceV2.atualizarCondominio(request));
    }
}
