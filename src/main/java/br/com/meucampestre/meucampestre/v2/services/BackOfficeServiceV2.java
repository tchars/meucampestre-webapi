package br.com.meucampestre.meucampestre.v2.services;

import br.com.meucampestre.meucampestre.domain.constants.TiposDePapeis;
import br.com.meucampestre.meucampestre.domain.models.*;
import br.com.meucampestre.meucampestre.repositories.*;
import br.com.meucampestre.meucampestre.v2.domain.exceptions.*;
import br.com.meucampestre.meucampestre.v2.domain.models.partials.UsuarioPapelCondominioPartial;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BackOfficeServiceV2 {

    private final CondominioRepo condominioRepo;
    private final UsuarioRepo usuarioRepo;
    private final PasswordEncoder encoder;
    private final PapelRepo papelRepo;
    private final UsuarioPapelCondominioRepo usuarioPapelCondominioRepo;
    private final UnidadeRepo unidadeRepo;
    private final UnidadeUsuarioRepo unidadeUsuarioRepo;
    private final CondominioUnidadeRepo condominioUnidadeRepo;

    // ------------------
    // Condomínio
    // ------------------
    public List<Condominio> buscarTodosCondominios() {

        List<Condominio> condominioList = condominioRepo.findAll();

        for (Condominio condominio : condominioList) {
            condominio.setUnidades(buscarTodasUnidadesDeUmCondominio(condominio.getId()));
            condominio.setUsuarios(gerarListaDePapeisDosUsuarios(condominio.getId()));
        }

        return condominioList;
    }

    public Condominio buscarCondominioPeloDocumento(String documento) {
        return condominioRepo
                .getCondominioPorDocumento(documento)
                .orElseThrow(() -> new CondominioNaoEncontradoException(documento));
    }

    public Condominio buscarCondominioPeloId(long idCondominio) {

        Condominio c = condominioRepo
                .findById(idCondominio)
                .orElseThrow(() -> new CondominioNaoEncontradoException(idCondominio));

        // Adiciono usuários e respectivos papeis ao retorno do objeto condominio
        List<UsuarioPapelCondominioPartial> upList = gerarListaDePapeisDosUsuarios(idCondominio);

        c.setUsuarios(upList);

        // Adiciono unidades ao objeto
        List<Unidade> unidades = buscarTodasUnidadesDeUmCondominio(idCondominio);

        c.setUnidades(unidades);

        return c;
    }

    private List<UsuarioPapelCondominioPartial> gerarListaDePapeisDosUsuarios(long idCondominio) {

        Collection<UsuarioPapelCondominio> usuarios =
                usuarioPapelCondominioRepo.buscarTodosUsuariosDeUmCondominio(idCondominio);

        Map<Usuario, List<UsuarioPapelCondominio>> groupByPriceMap =
                usuarios.stream().collect(Collectors.groupingBy(UsuarioPapelCondominio::getUsuario));

        List<UsuarioPapelCondominioPartial> upList = new ArrayList<>();

        // Aqui adiciono as roles para cada usuário do condomínio
        // já que um usuário pode ser várias coisas no mesmo condomínio
        for (Map.Entry<Usuario, List<UsuarioPapelCondominio>> entry : groupByPriceMap.entrySet())
        {
            UsuarioPapelCondominioPartial up = new UsuarioPapelCondominioPartial();

            up.setId(entry.getKey().getId());
            up.setNome(entry.getKey().getNome());
            up.setDocumento(entry.getKey().getDocumento());

            for (UsuarioPapelCondominio t : entry.getValue()) {
                up.getPapeis().add(t.getPapel().getNome());
            }

            upList.add(up);
        }

        return upList;
    }

    public Condominio salvarCondominio(Condominio novoCondominio)
    {
        boolean condominioExistente = condominioRepo
                                            .getCondominioPorDocumento(novoCondominio.getDocumento())
                                            .isPresent();

        if (condominioExistente)
        {
            throw new CondominioJaCadastradoException(novoCondominio.getDocumento());
        }

        return condominioRepo.save(novoCondominio);
    }

    public Condominio atualizarCondominio(Condominio novosDadosCondominio) {

        Condominio condominioAntigo =
                condominioRepo
                        .getCondominioPorDocumento(novosDadosCondominio.getDocumento())
                        .orElseThrow(() -> new CondominioNaoEncontradoException(
                                novosDadosCondominio.getDocumento()));

        condominioAntigo.setNome(novosDadosCondominio.getNome());
        condominioAntigo.setDescricao(novosDadosCondominio.getDescricao());
        condominioAntigo.setEmail(novosDadosCondominio.getEmail());
        condominioAntigo.setDocumento(novosDadosCondominio.getDocumento());
        condominioAntigo.setEndereco(novosDadosCondominio.getEndereco());
        condominioAntigo.setImagemUrl(novosDadosCondominio.getImagemUrl());
        condominioAntigo.setAtivo(novosDadosCondominio.getAtivo());

        return condominioRepo.save(condominioAntigo);
    }

    public Condominio deletarCondominio(long idCondominio) {

        Condominio condominioAntigo = condominioRepo
                .findById(idCondominio)
                .orElseThrow(() -> new CondominioNaoEncontradoException(idCondominio));

        condominioAntigo.setAtivo(false);

        return condominioRepo.save(condominioAntigo);
    }

    // ------------------
    // Usuário / Moradores
    // ------------------
    public List<Usuario> buscarTodoUsuarios() {
        return usuarioRepo.findAll();
    }

    public Usuario buscarUsuarioPeloDocumento(String documento) {
        return usuarioRepo
                .findByDocumento(documento)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(documento));
    }

    public Usuario buscarUsuarioPeloId(long id) {
        return usuarioRepo
                .findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(id));
    }

    public Usuario salvarUsuario(Usuario novoUsuario)
    {
        boolean usuarioExistente = usuarioRepo
                .findByDocumento(novoUsuario.getDocumento())
                .isPresent();

        if (usuarioExistente)
        {
            throw new UsuarioJaCadastradoException(novoUsuario.getDocumento());
        }

        novoUsuario.setSenha(encoder.encode(novoUsuario.getSenha()));

        return usuarioRepo.save(novoUsuario);
    }

    public Usuario atualizarDadosUsuario(Usuario novosDados) {

        Usuario usuarioAntigo = usuarioRepo
                .findByDocumento(novosDados.getDocumento())
                .orElseThrow(() -> new UsuarioNaoEncontradoException(novosDados.getDocumento()));

        usuarioAntigo.setNome(novosDados.getNome());
        usuarioAntigo.setEmail(novosDados.getEmail());
        usuarioAntigo.setSenha(encoder.encode(novosDados.getSenha()));
        usuarioAntigo.setDocumento(novosDados.getDocumento());
        usuarioAntigo.setTelefone(novosDados.getTelefone());
        usuarioAntigo.setImagemUrl(novosDados.getImagemUrl());
        usuarioAntigo.setAtivo(novosDados.getAtivo());

        return usuarioRepo.save(usuarioAntigo);
    }

    public Usuario deletarUsuario(long id) {

        Usuario antigo = usuarioRepo
                .findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(id));

        antigo.setAtivo(false);

        return usuarioRepo.save(antigo);
    }

    // ------------------
    // Papéis
    // ------------------
    public void adicionarPapelAoUsuario(long idUsuario, String nomePapel,
                                                          long idCondominio)
    {
        Usuario u = usuarioRepo.getById(idUsuario)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(idUsuario));

        Papel p = papelRepo.getByNome(nomePapel)
                .orElseThrow(() -> new PapelDesejadoNaoEncontradoException(nomePapel));

        Condominio c = condominioRepo.findById(idCondominio)
                    .orElseThrow(() -> new CondominioNaoEncontradoException(idCondominio));

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

    public Usuario buscarPapeisDoUsuario(long idUsuario, long idCondominio)
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

    public void adicionarPapelAoUsuarioBackoffice(long idUsuario)
    {
        Usuario u = usuarioRepo.getById(idUsuario)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(idUsuario));

        Papel p = papelRepo.getByNome(TiposDePapeis.BACKOFFICE)
                .orElseThrow(() -> new PapelDesejadoNaoEncontradoException(TiposDePapeis.BACKOFFICE));

        boolean usuarioExiste =
                usuarioPapelCondominioRepo
                        .getPorUsuarioCondominioPapel(u.getId(), null, p.getId())
                        .isPresent();

        if (usuarioExiste)
        {
            throw new UsuarioJaCadastradoComRoleParaOCondominioEscolhidoException(u.getDocumento(),
                    p.getNome(), null);
        }

        usuarioPapelCondominioRepo.save(new UsuarioPapelCondominio(null, u, null, p, true));
    }

    // ------------------
    // Unidades
    // ------------------
    public List<Unidade> buscarTodasUnidadesDeUmCondominio(long idCondominio)
    {
        boolean condominioExiste = condominioRepo
                                    .buscarPeloId(idCondominio)
                                    .isPresent();

        if (!condominioExiste) {
            throw new CondominioNaoEncontradoException(idCondominio);
        }

        List<CondominioUnidade> unidadesLink = condominioUnidadeRepo
                                            .buscarTodasUnidadesDeUmCondominioPeloId(idCondominio)
                                            .get();

        List<Unidade> unidades = new ArrayList<>();

        for (CondominioUnidade unidade : unidadesLink) {
            unidades.add(unidade.getUnidade());
        }

        return unidades;
    }

    public Unidade buscarUnidadeDeUmCondominio(long idCondominio, long idUnidade)
    {
        boolean condominioExiste = condominioRepo
                .buscarPeloId(idCondominio)
                .isPresent();

        if (!condominioExiste) {
            throw new CondominioNaoEncontradoException(idCondominio);
        }

        Optional<CondominioUnidade> condominioUnidade = condominioUnidadeRepo
                .buscarUnidadeDeUmCondominio(idCondominio, idUnidade);

        if (condominioUnidade.isEmpty()) {
            throw new UnidadeNaoEncontradaException(idUnidade);
        }

        return unidadeRepo.getById(condominioUnidade.get().getUnidade().getId());
    }

    public Unidade salvarUnidade(long idCondominio, Unidade unidade)
    {
        Optional<Condominio> condominioExiste = condominioRepo
                                                    .buscarPeloId(idCondominio);

        if (condominioExiste.isEmpty()) {
            throw new CondominioNaoEncontradoException(idCondominio);
        }

        Unidade unidadeSalva = unidadeRepo.save(unidade);

        condominioUnidadeRepo.save(new CondominioUnidade(null, condominioExiste.get(),
                unidadeSalva));

        return unidadeSalva;
    }

}
