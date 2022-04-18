package br.com.meucampestre.meucampestre.apimodels.usuarios;

import br.com.meucampestre.meucampestre.domain.models.Papel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CriarUsuarioResponse {

    private Long id;
    private String nome;
    private String documento;
    private List<Papel> papel;

    private Long idCondominio;
}
