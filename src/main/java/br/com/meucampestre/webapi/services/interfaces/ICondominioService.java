package br.com.meucampestre.webapi.services.interfaces;

import br.com.meucampestre.webapi.dto.base.PaginacaoBase;
import br.com.meucampestre.webapi.dto.condominio.CondominioDTO;

public interface ICondominioService {

    CondominioDTO criarCondominio(String email, CondominioDTO condominioDTO);

    PaginacaoBase<CondominioDTO> buscarTodosCondominios(int numeroDaPagina, int itensPorPagina, String ordenarPelo, String ordenarDeForma);

    CondominioDTO buscarCondominioPeloId(Long id);
}
