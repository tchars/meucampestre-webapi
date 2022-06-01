package br.com.meucampestre.meucampestre.v2.services;

import br.com.meucampestre.meucampestre.domain.models.*;
import br.com.meucampestre.meucampestre.repositories.*;
import br.com.meucampestre.meucampestre.v2.domain.exceptions.CondominioJaCadastradoException;
import br.com.meucampestre.meucampestre.v2.domain.exceptions.CondominioNaoEncontradoException;
import br.com.meucampestre.meucampestre.v2.domain.models.partials.UsuarioPapelCondominioPartial;
import br.com.meucampestre.meucampestre.v2.domain.models.partials.UsuarioUnidadePartial;
import br.com.meucampestre.meucampestre.v2.domain.models.partials.UsuariosDoCondominioPartial;
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
public class CondominioServiceV2 {

    private final CondominioRepo condominioRepo;
    private final UsuarioPapelCondominioRepo usuarioPapelCondominioRepo;
    private final UnidadeUsuarioRepo unidadeUsuarioRepo;
    private final CondominioUnidadeRepo condominioUnidadeRepo;

    public List<Condominio> buscarTodosCondominios(String usuario) {

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
        //TODO: TRAZER SOMENTE CONDOMÍNIO QUE FAÇO PARTE

        Condominio c = condominioRepo
                .findById(idCondominio)
                .orElseThrow(() -> new CondominioNaoEncontradoException(idCondominio));

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

    public List<UsuariosDoCondominioPartial> buscarTodosUsuariosDeUmCondominio(long idCondominio)
    {
        condominioRepo
                .buscarPeloId(idCondominio)
                .orElseThrow(() -> new CondominioNaoEncontradoException(idCondominio));

        List<UsuarioPapelCondominio> usuariosCondominio =
                usuarioPapelCondominioRepo.buscarTodosUsuariosDeUmCondominioAgrupados(idCondominio)
                        .get();

        List<UsuariosDoCondominioPartial> usuarioList = new ArrayList<>();
        for (UsuarioPapelCondominio usr : usuariosCondominio) {

            UsuariosDoCondominioPartial ss = new UsuariosDoCondominioPartial();

            ss.setId(usr.getUsuario().getId());
            ss.setNome(usr.getUsuario().getNome());
            ss.setDocumento(usr.getUsuario().getDocumento());
            ss.setImagemUrl(usr.getUsuario().getImagemUrl());

            usuarioList.add(ss);
        }

        return usuarioList;
    }

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

            unidade
                    .getUnidade()
                    .setUsuarios(gerarListaDeUsuariosDaUnidade(unidade.getUnidade().getId()));

            unidades.add(unidade.getUnidade());
        }

        return unidades;
    }

    private List<UsuarioUnidadePartial> gerarListaDeUsuariosDaUnidade(long idUnidade)
    {
        List<UnidadeUsuario> unsList = unidadeUsuarioRepo.buscarTodosUsuariosDeUmaUnidade(idUnidade)
                .get();

        List<UsuarioUnidadePartial> partialList = new ArrayList<>();

        for (UnidadeUsuario uns : unsList) {

            UsuarioUnidadePartial partial = new UsuarioUnidadePartial();

            partial.setId(uns.getUsuario().getId());
            partial.setNome(uns.getUsuario().getNome());
            partial.setDocumento(uns.getUsuario().getDocumento());

            partialList.add(partial);
        }

        return partialList;
    }
}
