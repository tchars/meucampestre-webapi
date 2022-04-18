package br.com.meucampestre.meucampestre.services;

import br.com.meucampestre.meucampestre.domain.models.Papel;
import br.com.meucampestre.meucampestre.repositories.PapelRepo;
import br.com.meucampestre.meucampestre.services.interfaces.IPapelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PapelService implements IPapelService {

    private final PapelRepo _papelRepo;

    @Override
    public Papel buscarPapelPeloNome(String nome) {
        return _papelRepo.findByNome(nome);
    }
}
