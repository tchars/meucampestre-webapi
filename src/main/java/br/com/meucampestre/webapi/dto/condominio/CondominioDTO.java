package br.com.meucampestre.webapi.dto.condominio;

import br.com.meucampestre.webapi.dto.chacara.ChacaraDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CondominioDTO {

    private Long id;

    @NotEmpty
    @Size(min = 2, message = "O nome do condomínio deve ter pelo menos 2 caracteres")
    private String nomeCondominio;

    private String descricao;

    private List<ChacaraDTO> chacaras;
}
