package br.com.meucampestre.webapi.dto.condominio;

import br.com.meucampestre.webapi.dto.chacara.ChacaraDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CondominioDTO {

    private Long id;

    private String nomeCondominio;

    private String descricao;

    private List<ChacaraDTO> chacaras;
}
