package br.com.meucampestre.meucampestre.v2.services;

import br.com.meucampestre.meucampestre.domain.models.Condominio;
import br.com.meucampestre.meucampestre.repositories.CondominioRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CondominioServiceV2 {

    private final CondominioRepo condominioRepo;

    public List<Condominio> buscarTodosCondominiosDoUsuario(long idUsuario)
    {
        return condominioRepo.findAll();
    }

}
