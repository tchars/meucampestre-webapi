package br.com.meucampestre.webapi.services.interfaces;

import br.com.meucampestre.webapi.dto.base.PaginacaoBase;
import br.com.meucampestre.webapi.dto.chacara.ChacaraDTO;

public interface IChacaraService {

    ChacaraDTO criarChacara(ChacaraDTO chacaraDTO);

    PaginacaoBase<ChacaraDTO> buscarTodasChacaras(int numeroDaPagina, int itensPorPagina, String ordenarPelo, String ordenarDeForma);

    ChacaraDTO buscarChacaraPeloId(Long id);
}
