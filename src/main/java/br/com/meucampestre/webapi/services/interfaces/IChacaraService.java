package br.com.meucampestre.webapi.services.interfaces;

import br.com.meucampestre.webapi.dto.Chacara.PaginacaoChacaras;
import br.com.meucampestre.webapi.dto.Chacara.ChacaraDTO;

public interface IChacaraService {

    ChacaraDTO criarChacara(ChacaraDTO chacaraDTO);

    PaginacaoChacaras buscarTodasChacaras(int numeroDaPagina, int itensPorPagina, String ordenarPelo, String ordenarDeForma);

    ChacaraDTO buscarChacaraPeloId(Long id);
}
