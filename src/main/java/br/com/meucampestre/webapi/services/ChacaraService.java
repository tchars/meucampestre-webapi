package br.com.meucampestre.webapi.services;

import br.com.meucampestre.webapi.dto.base.PaginacaoBase;
import br.com.meucampestre.webapi.entities.Condominio;
import br.com.meucampestre.webapi.exceptions.RecursoNaoEncontradoException;
import br.com.meucampestre.webapi.repositories.ICondominioRepository;
import br.com.meucampestre.webapi.dto.chacara.ChacaraDTO;
import br.com.meucampestre.webapi.entities.Chacara;
import br.com.meucampestre.webapi.repositories.IChacaraRepository;
import br.com.meucampestre.webapi.services.interfaces.IChacaraService;
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
public class ChacaraService implements IChacaraService {

    private final IChacaraRepository _chacaraRepository;

    private final ICondominioRepository _condominioRepository;

    private final ModelMapper _modelMapper;

    @Autowired
    public ChacaraService(IChacaraRepository chacaraRepository, ICondominioRepository condominioRepository, ModelMapper modelMapper) {
        _chacaraRepository = chacaraRepository;
        _condominioRepository = condominioRepository;
        _modelMapper = modelMapper;
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
        ChacaraDTO chacaraCriadaDTO = _modelMapper.map(chacaraCriada, ChacaraDTO.class);

        return chacaraCriadaDTO;
    }

    @Override
    public PaginacaoBase<ChacaraDTO> buscarTodasChacaras(int numeroDaPagina, int itensPorPagina, String ordenarPelo, String ordenarDeForma) {

        Sort parametrosOrdenacao = ordenarDeForma.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(ordenarPelo).ascending()
                : Sort.by(ordenarPelo).descending();

        Pageable pageable = PageRequest.of(numeroDaPagina, itensPorPagina, parametrosOrdenacao);

        Page<Chacara> chacaras = _chacaraRepository.findAll(pageable);

        List<Chacara> chacaraDTOList = chacaras.getContent();

        List<ChacaraDTO> lista = chacaraDTOList
                                    .stream()
                                    .map(chacara -> _modelMapper.map(chacara, ChacaraDTO.class))
                                    .collect(Collectors.toList());

        PaginacaoBase<ChacaraDTO> paginacaoChacaras = new PaginacaoBase<>();

        paginacaoChacaras.setResultados(lista);
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

        return _modelMapper.map(chacara, ChacaraDTO.class);
    }

    // Converter DTO -> Entidade
    private Chacara mapearEntidade(ChacaraDTO chacaraDTO, Condominio condominio) {

        Chacara chacara = _modelMapper.map(chacaraDTO, Chacara.class);

        chacara.setCondominio(condominio);

        return chacara;
    }
}
