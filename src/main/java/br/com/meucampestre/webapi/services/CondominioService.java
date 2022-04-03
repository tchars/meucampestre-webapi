package br.com.meucampestre.webapi.services;

import br.com.meucampestre.webapi.dto.base.PaginacaoBase;
import br.com.meucampestre.webapi.dto.chacara.ChacaraDTO;
import br.com.meucampestre.webapi.entities.Chacara;
import br.com.meucampestre.webapi.entities.Condominio;
import br.com.meucampestre.webapi.exceptions.RecursoNaoEncontradoException;
import br.com.meucampestre.webapi.repositories.ICondominioRepository;
import br.com.meucampestre.webapi.dto.condominio.CondominioDTO;
import br.com.meucampestre.webapi.repositories.IChacaraRepository;
import br.com.meucampestre.webapi.services.interfaces.ICondominioService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CondominioService implements ICondominioService {

    private final ICondominioRepository _condominioRepository;

    private final IChacaraRepository _chacaraRepository;

    private final ModelMapper _modelMapper;

    @Autowired
    public CondominioService(ICondominioRepository condominioRepository, IChacaraRepository chacaraRepository, ModelMapper modelMapper) {
        _condominioRepository = condominioRepository;
        _chacaraRepository = chacaraRepository;
        _modelMapper = modelMapper;
    }

    // Apenas crio um condomínio
    @Override
    public CondominioDTO criarCondominio(CondominioDTO condominioDTO) {

        // Convertendo de DTO -> Entidade
        Condominio condominio = mapearEntidade(condominioDTO);

        // Persistimos
        Condominio condominioCriado = _condominioRepository.save(condominio);

        // Convertemos Entidade -> DTO
        CondominioDTO condominioCriadoDTO = mapearDTO(condominioCriado);

        return condominioCriadoDTO;
    }

    // Listo condomínios
    @Override
    public PaginacaoBase<CondominioDTO> buscarTodosCondominios(int numeroDaPagina, int itensPorPagina, String ordenarPelo, String ordenarDeForma) {

        Sort parametrosOrdenacao = ordenarDeForma.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(ordenarPelo).ascending()
                : Sort.by(ordenarPelo).descending();

        Pageable pageable = PageRequest.of(numeroDaPagina, itensPorPagina, parametrosOrdenacao);

        Page<Condominio> condominios = _condominioRepository.findAll(pageable);

        List<Condominio> condominiosDTOList = condominios.getContent();

        List<CondominioDTO> listaDeCondominios = condominiosDTOList
                .stream()
                .map(condominio -> mapearDTO(condominio))
                .collect(Collectors.toList());

        PaginacaoBase<CondominioDTO> paginacaoCondominio = new PaginacaoBase<>();

        paginacaoCondominio.setResultados(listaDeCondominios);
        paginacaoCondominio.setNumeroDaPagina(condominios.getNumber());
        paginacaoCondominio.setItensPorPagina(condominios.getSize());
        paginacaoCondominio.setTotalDeElementos(condominios.getTotalElements());
        paginacaoCondominio.setTotalDePaginas(condominios.getTotalPages());
        paginacaoCondominio.setEstaNaUltima(condominios.isLast());

        return paginacaoCondominio;
    }

    // Busco somente um condomínio
    @Override
    public CondominioDTO buscarCondominioPeloId(Long id) {

        Condominio condominio = _condominioRepository
                .findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Condominio", "id", id));

        return mapearDTO(condominio);
    }

    // Converter Entidade -> DTO
    private CondominioDTO mapearDTO(Condominio condominio) {

        CondominioDTO condominioDTO = _modelMapper.map(condominio, CondominioDTO.class);

        List<ChacaraDTO> chacaraDTOList = condominio.getChacaras()
                .stream()
                .map(chacara -> _modelMapper.map(chacara, ChacaraDTO.class))
                .collect(Collectors.toList());

        condominioDTO.setChacaras(chacaraDTOList);

        return condominioDTO;
    }

    // Converter DTO -> Entidade
    private Condominio mapearEntidade(CondominioDTO condominioDTO) {

        Condominio condominio = new Condominio();

        condominio.setId(condominioDTO.getId());
        condominio.setNomeCondominio(condominioDTO.getNomeCondominio());
        condominio.setDescricao(condominioDTO.getDescricao());

        return condominio;
    }
}
