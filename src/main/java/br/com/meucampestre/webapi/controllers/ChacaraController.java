package br.com.meucampestre.webapi.controllers;

import br.com.meucampestre.webapi.dto.Chacara.PaginacaoChacaras;
import br.com.meucampestre.webapi.utils.Constantes;
import br.com.meucampestre.webapi.dto.Chacara.ChacaraDTO;
import br.com.meucampestre.webapi.services.interfaces.IChacaraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chacaras")
public class ChacaraController {

    private final IChacaraService _chacaraService;

    @Autowired
    public ChacaraController(IChacaraService chacaraService) {
        _chacaraService = chacaraService;
    }

    // Crio uma chácara
    @PostMapping
    public ResponseEntity<ChacaraDTO> criarChacara(@RequestBody ChacaraDTO chacaraDTO) {
        return new ResponseEntity<>(_chacaraService.criarChacara(chacaraDTO), HttpStatus.CREATED);
    }

    // Listar todas chácaras
    @GetMapping
    public ResponseEntity<PaginacaoChacaras> buscarTodasChacaras(
            @RequestParam(value = "numeroDaPagina", defaultValue = Constantes.NUMERO_DA_PAGINA_PADRAO, required = false) int numeroDaPagina,
            @RequestParam(value = "itensPorPagina", defaultValue = Constantes.QUANTIDADE_DE_ITENS_POR_PAGINA_PADRAO, required = false) int itensPorPagina,
            @RequestParam(value = "ordenarPelo", defaultValue = Constantes.COLUNA_DE_ORDENACAO_PADRAO, required = false) String ordenarPelo,
            @RequestParam(value = "ordenarDeForma", defaultValue = Constantes.TIPO_DE_ORDENACAO_PADRAO, required = false) String ordenarDeForma)
    {
        return new ResponseEntity<>(_chacaraService.buscarTodasChacaras(numeroDaPagina, itensPorPagina, ordenarPelo, ordenarDeForma), HttpStatus.OK);
    }

    // Listar uma chácara pelo ID
    @GetMapping("/{id}")
    public ResponseEntity<ChacaraDTO> buscarChacaraPeloId(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(_chacaraService.buscarChacaraPeloId(id));
    }
}
