package br.com.meucampestre.webapi.services;

import br.com.meucampestre.webapi.dto.Chacara.PaginacaoChacaras;
import br.com.meucampestre.webapi.entities.Condominio;
import br.com.meucampestre.webapi.exceptions.RecursoNaoEncontradoException;
import br.com.meucampestre.webapi.repositories.ICondominioRepository;
import br.com.meucampestre.webapi.dto.Chacara.ChacaraDTO;
import br.com.meucampestre.webapi.entities.Chacara;
import br.com.meucampestre.webapi.repositories.IChacaraRepository;
import br.com.meucampestre.webapi.services.interfaces.IChacaraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChacaraService implements IChacaraService {

    private final IChacaraRepository _chacaraRepository;

    private final ICondominioRepository _condominioRepository;

    @Autowired
    public ChacaraService(IChacaraRepository chacaraRepository, ICondominioRepository condominioRepository) {
        _chacaraRepository = chacaraRepository;
        _condominioRepository = condominioRepository;
    }

    @Override
    public ChacaraDTO criarChacara(ChacaraDTO chacaraDTO) {

        // Verifico se o condomínio a ser dono da chácara existe
        Condominio condominio = _condominioRepository.findById(chacaraDTO.getIdCondominio())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Condominio", "id", chacaraDTO.getIdCondominio()));

        // Convertendo de DTO -> Entidade
        Chacara chacara = mapearEntidade(chacaraDTO, condominio);

        // Persistimos
        Chacara chacaraCriada = _chacaraRepository.save(chacara);

        // Convertemos Entidade -> DTO
        ChacaraDTO chacaraCriadaDTO = mapearDTO(chacaraCriada);

        return chacaraCriadaDTO;
    }

    @Override
    public PaginacaoChacaras buscarTodasChacaras(int numeroDaPagina, int itensPorPagina, String ordenarPelo, String ordenarDeForma) {

        Sort parametrosOrdenacao = ordenarDeForma.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(ordenarPelo).ascending()
                : Sort.by(ordenarPelo).descending();

        Pageable pageable = PageRequest.of(numeroDaPagina, itensPorPagina, parametrosOrdenacao);

        Page<Chacara> chacaras = _chacaraRepository.findAll(pageable);

        List<Chacara> chacaraDTOList = chacaras.getContent();

        List<ChacaraDTO> lista = chacaraDTOList
                                    .stream()
                                    .map(chacara -> mapearDTO(chacara))
                                    .collect(Collectors.toList());

        PaginacaoChacaras paginacaoChacaras = new PaginacaoChacaras();

        paginacaoChacaras.setConteudo(lista);
        paginacaoChacaras.setNumeroDaPagina(chacaras.getNumber());
        paginacaoChacaras.setItensPorPagina(chacaras.getSize());
        paginacaoChacaras.setTotalDeElementos(chacaras.getTotalElements());
        paginacaoChacaras.setTotalDePaginas(chacaras.getTotalPages());
        paginacaoChacaras.setEstaNaUltima(chacaras.isLast());

        return paginacaoChacaras;
    }

    @Override
    public ChacaraDTO buscarChacaraPeloId(Long id) {

        Chacara chacara = _chacaraRepository
                            .findById(id)
                            .orElseThrow(() -> new RecursoNaoEncontradoException("Chacara", "id", id));

        return mapearDTO(chacara);
    }

    // Converter Entidade -> DTO
    private ChacaraDTO mapearDTO(Chacara chacara) {

        ChacaraDTO chacaraDTO = new ChacaraDTO();

        chacaraDTO.setId(chacara.getId());
        chacaraDTO.setTitulo(chacara.getTitulo());
        chacaraDTO.setIdentificador(chacara.getIdentificador());
        chacaraDTO.setDescricao(chacara.getDescricao());
        chacaraDTO.setObservacao(chacara.getObservacao());

        chacaraDTO.setIdCondominio(chacara.getCondominio().getId());
        chacaraDTO.setCriadoEm(chacara.getCriadoEm());

        return chacaraDTO;
    }

    // Converter DTO -> Entidade
    private Chacara mapearEntidade(ChacaraDTO chacaraDTO, Condominio condominio) {

        Chacara chacara = new Chacara();

        chacara.setId(chacaraDTO.getId());
        chacara.setTitulo(chacaraDTO.getTitulo());
        chacara.setIdentificador(chacaraDTO.getIdentificador());
        chacara.setDescricao(chacaraDTO.getDescricao());
        chacara.setObservacao(chacaraDTO.getObservacao());

        chacara.setCondominio(condominio);

        return chacara;
    }
}
