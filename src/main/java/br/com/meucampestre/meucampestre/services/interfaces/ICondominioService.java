package br.com.meucampestre.meucampestre.services.interfaces;

import br.com.meucampestre.meucampestre.domain.models.Condominio;
import br.com.meucampestre.meucampestre.domain.models.Usuario;

import java.util.Collection;
import java.util.List;

public interface ICondominioService {

    // Contexto Condomínio
    Condominio salvarCondominio(Condominio condominio);
    List<Condominio> buscarTodosCondominios();
    Condominio buscarCondominio(Long id);

    // Contexto Usuários
    Collection<Usuario> buscarTodosUsuariosDeUmCondominio(Long idCondominio);
}
