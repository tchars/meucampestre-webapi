package br.com.meucampestre.webapi.controllers;

import br.com.meucampestre.webapi.dto.Chacara.ChacaraDTO;
import br.com.meucampestre.webapi.utils.Constantes;
import br.com.meucampestre.webapi.dto.Condominios.CondominioDTO;
import br.com.meucampestre.webapi.dto.Condominios.PaginacaoCondominio;
import br.com.meucampestre.webapi.services.interfaces.ICondominioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/condominios")
public class CondominioController {

    private final ICondominioService _condominioService;

    @Autowired
    public CondominioController(ICondominioService condominioService) {
        _condominioService = condominioService;
    }

    // Crio um condomínio
    @PostMapping
    public ResponseEntity<CondominioDTO> criarChacara(@RequestBody CondominioDTO condominioDTO) {
        return new ResponseEntity<>(_condominioService.criarCondominio(condominioDTO), HttpStatus.CREATED);
    }

    // Listar todos condominios
    @GetMapping
    public ResponseEntity<PaginacaoCondominio> buscarTodosCondominios(
            @RequestParam(value = "numeroDaPagina", defaultValue = Constantes.NUMERO_DA_PAGINA_PADRAO, required = false) int numeroDaPagina,
            @RequestParam(value = "itensPorPagina", defaultValue = Constantes.QUANTIDADE_DE_ITENS_POR_PAGINA_PADRAO, required = false) int itensPorPagina,
            @RequestParam(value = "ordenarPelo", defaultValue = Constantes.COLUNA_DE_ORDENACAO_PADRAO, required = false) String ordenarPelo,
            @RequestParam(value = "ordenarDeForma", defaultValue = Constantes.TIPO_DE_ORDENACAO_PADRAO, required = false) String ordenarDeForma)
    {
        return new ResponseEntity<>(_condominioService.buscarTodosCondominios(numeroDaPagina, itensPorPagina, ordenarPelo, ordenarDeForma), HttpStatus.OK);
    }

    // Listar condominio pelo ID
    @GetMapping("/{id}")
    public ResponseEntity<CondominioDTO> buscarCondominioPeloId(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(_condominioService.buscarCondominioPeloId(id));
    }
}
