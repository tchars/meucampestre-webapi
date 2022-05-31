package br.com.meucampestre.meucampestre.v2.domain.models;

import br.com.meucampestre.meucampestre.v2.domain.models.partials.CondominioPartial;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MinhaConta
{
    private Long id;

    private String nome;

    private String email;

    private String documento;

    private String telefone;

    private String imagemUrl;

    private Boolean ativo;

    private List<CondominioPartial> condominios = new ArrayList<>();
}
