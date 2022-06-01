package br.com.meucampestre.meucampestre.v2.services;

import br.com.meucampestre.meucampestre.domain.models.Papel;
import br.com.meucampestre.meucampestre.domain.models.Usuario;
import br.com.meucampestre.meucampestre.domain.models.UsuarioPapelCondominio;
import br.com.meucampestre.meucampestre.repositories.UsuarioPapelCondominioRepo;
import br.com.meucampestre.meucampestre.repositories.UsuarioRepo;
import br.com.meucampestre.meucampestre.v2.apiModels.requests.AtualizarMeusDadosDaContaRequest;
import br.com.meucampestre.meucampestre.v2.domain.exceptions.UsuarioNaoEncontradoException;
import br.com.meucampestre.meucampestre.v2.domain.models.MinhaConta;
import br.com.meucampestre.meucampestre.v2.domain.models.partials.CondominioPartial;
import br.com.meucampestre.meucampestre.v2.domain.models.partials.UsuarioPapelCondominioPartial;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MinhaContaServiceV2 {

    private final UsuarioRepo usuarioRepo;
    private final UsuarioPapelCondominioRepo usuarioPapelCondominioRepo;
    private final PasswordEncoder encoder;

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

    public Usuario atualizarMeusDados(String usuarioDoToken, AtualizarMeusDadosDaContaRequest request) {

        Usuario usr = usuarioRepo.findByDocumento(usuarioDoToken)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(usuarioDoToken));

        usr.setNome(request.getNome());
        usr.setEmail(request.getEmail());
        usr.setSenha(encoder.encode(request.getSenha()));
        usr.setTelefone(request.getTelefone());
        usr.setImagemUrl(request.getImagemUrl());

        return usr;
    }

    public List<Papel> buscarRolesAPartirDoCondominio(String usuarioDoToken, long idCondominio) {

        Usuario usr = usuarioRepo.findByDocumento(usuarioDoToken)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(usuarioDoToken));

        List<UsuarioPapelCondominio> link = usuarioPapelCondominioRepo
                .buscarPapeisPorUsuarioECondominio(usr.getId(), idCondominio)
                .get();


        List<Papel> papeis = new ArrayList<>();

        for (UsuarioPapelCondominio ll : link)
        {
            papeis.add(ll.getPapel());
        }

        return papeis;
    }
}
