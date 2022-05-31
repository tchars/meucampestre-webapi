package br.com.meucampestre.meucampestre.v2.services;

import br.com.meucampestre.meucampestre.domain.models.Usuario;
import br.com.meucampestre.meucampestre.domain.models.UsuarioPapelCondominio;
import br.com.meucampestre.meucampestre.repositories.UsuarioPapelCondominioRepo;
import br.com.meucampestre.meucampestre.repositories.UsuarioRepo;
import br.com.meucampestre.meucampestre.v2.domain.exceptions.UsuarioNaoEncontradoException;
import br.com.meucampestre.meucampestre.v2.domain.models.MinhaConta;
import br.com.meucampestre.meucampestre.v2.domain.models.partials.CondominioPartial;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MinhaContaServiceV2 {

    private final UsuarioRepo usuarioRepo;
    private final UsuarioPapelCondominioRepo usuarioPapelCondominioRepo;

    public MinhaConta buscarDadosDoUsuario(String documentoUsuario)
    {
        MinhaConta mc = new MinhaConta();

        Usuario usuario = usuarioRepo
                .findByDocumento(documentoUsuario)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(documentoUsuario));

        mc.setAtivo(usuario.getAtivo());
        mc.setDocumento(usuario.getDocumento());
        mc.setEmail(usuario.getEmail());
        mc.setId(usuario.getId());
        mc.setTelefone(usuario.getTelefone());
        mc.setImagemUrl(usuario.getImagemUrl());
        mc.setNome(usuario.getNome());

        Collection<UsuarioPapelCondominio> link = usuarioPapelCondominioRepo
                .buscarPorUsuarioAgrupado(usuario.getId());

        List<CondominioPartial> cpList = new ArrayList<>();

        for (UsuarioPapelCondominio ll : link)
        {
            if (ll.getCondominio() == null)
            {
                continue;
            }

            CondominioPartial ccp = new CondominioPartial();

            ccp.setId(ll.getCondominio().getId());
            ccp.setNome(ll.getCondominio().getNome());
            ccp.setImagemUrl(ll.getCondominio().getImagemUrl());

            cpList.add(ccp);
        }

        mc.setCondominios(cpList);

        return mc;
    }
}
