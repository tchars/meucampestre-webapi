package br.com.meucampestre.meucampestre.services.interfaces;

import br.com.meucampestre.meucampestre.domain.models.Papel;

public interface IPapelService {
    Papel buscarPapelPeloNome(String nome);
}
