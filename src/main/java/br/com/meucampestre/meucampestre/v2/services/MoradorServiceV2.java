package br.com.meucampestre.meucampestre.v2.services;

import br.com.meucampestre.meucampestre.apimodels.condominio.partials.MoradorDoCondominio;
import br.com.meucampestre.meucampestre.apimodels.usuarios.CriarUsuarioRequestV2;
import br.com.meucampestre.meucampestre.domain.models.*;
import br.com.meucampestre.meucampestre.repositories.*;
import br.com.meucampestre.meucampestre.v2.domain.exceptions.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class MoradorServiceV2 {

    private final UsuarioPapelCondominioRepo _usuarioPapelCondominioRepo;
    private final UsuarioRepo usuarioRepo;
    private final CondominioRepo condominioRepo;
    private final PapelRepo papelRepo;
    private final UsuarioPapelCondominioRepo usuarioPapelCondominioRepo;
    private final PasswordEncoder passwordEncoder;
    private final UnidadeRepo unidadeRepo;
    private final UnidadeUsuarioRepo unidadeUsuarioRepo;

    public ArrayList<MoradorDoCondominio> buscarTodosUsuariosDeUmCondominio(long idCondominio) {
        // TODO: Descobrir quem esta solicitando, se for tipo SINDICO ele deve ver todos,
        //  se for MORADOR/CONSELHEIRO ele deve ver apenas os que moram na sua

        Collection<UsuarioPapelCondominio> usuariosPapelCondominio =
                _usuarioPapelCondominioRepo.buscarTodosUsuariosDeUmCondominio(idCondominio);

        ArrayList<MoradorDoCondominio> moradoresDoCondominio = new ArrayList<>();

        for (UsuarioPapelCondominio usr : usuariosPapelCondominio)
        {
            Usuario usuario = usr.getUsuario();
            if(usuario.getAtivo()){
                ArrayList<String> papeis = new ArrayList<>();
                papeis.add(usr.getPapel().getNome());

                boolean added = false;

                for (MoradorDoCondominio moradorCondominioAdicionado : moradoresDoCondominio) {
                    if (moradorCondominioAdicionado.getDocumento().equals(usuario.getDocumento())){
                        moradorCondominioAdicionado.getTipoDePerfil().addAll(papeis);
                        added = true;
                    }
                }

                if(!added){
                    MoradorDoCondominio moradorDoCondominio = new MoradorDoCondominio(usuario);
                    moradorDoCondominio.setTipoDePerfil(papeis);
                    moradoresDoCondominio.add(moradorDoCondominio);
                }

            }
        }
        return moradoresDoCondominio;
    }

    public Usuario criarUsuario(long idCondominio, CriarUsuarioRequestV2 request) {

        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(request.getNome());
        novoUsuario.setEmail(request.getEmail());
        novoUsuario.setDocumento(request.getDocumento());
        novoUsuario.setSenha(passwordEncoder.encode(request.getSenha()));
        novoUsuario.setTelefone(request.getTelefone());
        novoUsuario.setAtivo(true);

        Usuario usuario = salvarUsuario(novoUsuario);

        Condominio condominio = condominioRepo
                .findById(idCondominio)
                .orElseThrow(() -> new CondominioNaoEncontradoException(idCondominio));

        if (request.getPapeis() != null)
        {
            for (String papel : request.getPapeis())
            {
                adicionarPapelAoUsuario(usuario, papel, condominio);
            }
        }

        if (request.getUnidades() != null)
        {
            for (Long unidadeId : request.getUnidades())
            {
                adicionarUsuarioAUnidade(unidadeId, usuario);
            }
        }

        return usuario;
    }

    //TODO: Esse metodo tbm irá precisar trazer as uniades do usuário
    public Usuario buscarUmUsuarioEmUmCondominio(long idCondominio, String documento) {
        Usuario usuario = buscarUsuarioPeloDocumento(documento);

        Usuario usuarioPapeis = buscarPapeisDoUsuario(usuario.getId(), idCondominio);

        if (usuarioPapeis.getPapeis().size() > 0) {
            return usuarioPapeis;
        }
        throw new UsuarioNaoCadastradoNoCondominioException(usuario.getDocumento());
    }

    public Usuario atualizarDadosUsuario(long idCondominio, CriarUsuarioRequestV2 request, String documento) {

        Usuario usuario = buscarUmUsuarioEmUmCondominio(idCondominio, documento);
        usuario.setNome(request.getNome());
        usuario.setEmail(request.getEmail());
        usuario.setDocumento(request.getDocumento());
        usuario.setTelefone(request.getTelefone());
        usuario.setAtivo(true);

        usuarioRepo.save(usuario);

        Condominio condominio = condominioRepo.findById(idCondominio)
                .orElseThrow(() -> new CondominioNaoEncontradoException(idCondominio));

        if(request.getPapeis() != null) {
            usuarioPapelCondominioRepo.apagarTodasPermissoesDoUsuarioAoCondominio(usuario.getId(), idCondominio);
            for (String papel : request.getPapeis()) {
                adicionarPapelAoUsuario(usuario, papel, condominio);
            }
        }

        // Como o retorno do editar não é usado no front, por enquanto está setando o papeis como lista vazia apenas para evitar de demonstrar os papeis antes do update
        usuario.setPapeis(new ArrayList<>());

        if(request.getUnidades() != null) {
            //TODO: Precisa atualizar as unidades do usuario conforme as unidades recebidas no request
        }
        return usuario;
    }

    public void removerPermissoesUsuario(long idCondominio, String documento) {
        Usuario usuario = buscarUmUsuarioEmUmCondominio(idCondominio, documento);
        usuarioPapelCondominioRepo.apagarTodasPermissoesDoUsuarioAoCondominio(usuario.getId(), idCondominio);
    }

    private Usuario salvarUsuario(Usuario novoUsuario)
    {
        boolean usuarioExistente = usuarioRepo
                .findByDocumento(novoUsuario.getDocumento())
                .isPresent();

        if (usuarioExistente)
        {
            throw new UsuarioJaCadastradoException(novoUsuario.getDocumento());
        }

        novoUsuario.setSenha(passwordEncoder.encode(novoUsuario.getSenha()));

        return usuarioRepo.save(novoUsuario);
    }

    private void adicionarPapelAoUsuario(Usuario u, String nomePapel, Condominio c)
    {

        Papel p = papelRepo.getByNome(nomePapel)
                .orElseThrow(() -> new PapelDesejadoNaoEncontradoException(nomePapel));

        boolean usuarioExiste =
                usuarioPapelCondominioRepo
                        .getPorUsuarioCondominioPapel(u.getId(), c.getId(), p.getId())
                        .isPresent();

        if (usuarioExiste)
        {
            throw new UsuarioJaCadastradoComRoleParaOCondominioEscolhidoException(u.getDocumento(),
                    p.getNome(), c.getNome());
        }

        usuarioPapelCondominioRepo.save(new UsuarioPapelCondominio(null, u, c, p, false));
    }

    private Usuario buscarUsuarioPeloDocumento(String documento) {
        return usuarioRepo
                .findByDocumento(documento)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(documento));
    }

    private Usuario buscarPapeisDoUsuario(long idUsuario, long idCondominio)
    {
        Usuario u = usuarioRepo.getById(idUsuario)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(idUsuario));

        Condominio c = condominioRepo.findById(idCondominio)
                .orElseThrow(() -> new CondominioNaoEncontradoException(idCondominio));

        List<UsuarioPapelCondominio> usuarioPapeisCondominio =
                usuarioPapelCondominioRepo.buscarPapeisPorUsuarioECondominio(u.getId(),
                                c.getId())
                        .get();

        for (UsuarioPapelCondominio link : usuarioPapeisCondominio)
        {
            u.getPapeis().add(link.getPapel());
        }

        return u;
    }

    private void adicionarUsuarioAUnidade(long idUnidade, Usuario usuario) {

        Unidade unidade = unidadeRepo.findById(idUnidade)
                .orElseThrow(() -> new UnidadeNaoEncontradaException(idUnidade));

        unidadeUsuarioRepo.save(new UnidadeUsuario(null, usuario, unidade));
    }
}
