package br.com.meucampestre.meucampestre.v2.services;

import br.com.meucampestre.meucampestre.domain.models.*;
import br.com.meucampestre.meucampestre.repositories.*;
import br.com.meucampestre.meucampestre.v2.domain.exceptions.*;
import br.com.meucampestre.meucampestre.v2.domain.models.partials.UsuarioUnidadePartial;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UnidadeServiceV2 {

    private final CondominioRepo condominioRepo;
    private final UsuarioRepo usuarioRepo;
    private final UnidadeRepo unidadeRepo;
    private final UnidadeUsuarioRepo unidadeUsuarioRepo;
    private final CondominioUnidadeRepo condominioUnidadeRepo;

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

            unidade
                    .getUnidade()
                    .setUsuarios(gerarListaDeUsuariosDaUnidade(unidade.getUnidade().getId()));

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

        Unidade unn = unidadeRepo.getById(condominioUnidade.get().getUnidade().getId());

        List<UsuarioUnidadePartial> partialList = gerarListaDeUsuariosDaUnidade(idUnidade);

        unn.setUsuarios(partialList);

        return unn;
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

    public UnidadeUsuario adicionarUsuarioAUnidade(long idCondominio,
                                                   long idUnidade,
                                                   String documentoUsuario)
    {
        condominioUnidadeRepo
                .buscarUnidadeDeUmCondominio(idCondominio, idUnidade)
                .orElseThrow(() -> new RelacaoEntreUnidadeECondominioNaoEncontradaException(idUnidade, idCondominio));

        Usuario usr = usuarioRepo
                .findByDocumento(documentoUsuario)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(documentoUsuario));

        Optional<UnidadeUsuario> udu = unidadeUsuarioRepo
                .buscarPorUnidadeUsuario(idUnidade, usr.getId());

        if (udu.isPresent())
        {
            throw new RelacaoEntreUnidadeEUsuarioJaExisteException(usr.getDocumento(), idUnidade);
        }

        Unidade unidade = unidadeRepo.getById(idUnidade);

        return unidadeUsuarioRepo.save(new UnidadeUsuario(null, usr, unidade));
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

    public Unidade atualizarDadosUnidade(long idCondominio, long idUnidade, Unidade novosDados) {

        condominioUnidadeRepo
                .buscarUnidadeDeUmCondominio(idCondominio, idUnidade)
                .orElseThrow(() -> new RelacaoEntreUnidadeECondominioNaoEncontradaException(idUnidade, idCondominio));

        Unidade unidadeAntiga = unidadeRepo.findById(idUnidade)
                .orElseThrow(() -> new UnidadeNaoEncontradaException(idUnidade));

        unidadeAntiga.setTitulo(novosDados.getTitulo());
        unidadeAntiga.setEndereco(novosDados.getEndereco());
        unidadeAntiga.setDescricao(novosDados.getDescricao());

        return unidadeRepo.saveAndFlush(unidadeAntiga);
    }

    public void excluirUnidade(long idCondominio, long idUnidade)
    {
        CondominioUnidade condominioUnidade = condominioUnidadeRepo
                .buscarUnidadeDeUmCondominio(idCondominio, idUnidade)
                .orElseThrow(() -> new RelacaoEntreUnidadeECondominioNaoEncontradaException(idUnidade, idCondominio));

        Unidade unidade = unidadeRepo.findById(idUnidade)
                .orElseThrow(() -> new UnidadeNaoEncontradaException(idUnidade));

        condominioUnidadeRepo.delete(condominioUnidade);
        unidadeRepo.delete(unidade);
    }
}
