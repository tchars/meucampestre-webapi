package br.com.meucampestre.webapi.services.interfaces;

import br.com.meucampestre.webapi.dto.Condominios.CondominioDTO;
import br.com.meucampestre.webapi.dto.Condominios.PaginacaoCondominio;

public interface ICondominioService {

    CondominioDTO criarCondominio(CondominioDTO condominioDTO);

    PaginacaoCondominio buscarTodosCondominios(int numeroDaPagina, int itensPorPagina, String ordenarPelo, String ordenarDeForma);

    CondominioDTO buscarCondominioPeloId(Long id);
}
