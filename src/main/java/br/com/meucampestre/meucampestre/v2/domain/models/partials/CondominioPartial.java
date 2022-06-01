package br.com.meucampestre.meucampestre.v2.domain.models.partials;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CondominioPartial
{
    private long id;
    private String nome;
    private String imagemUrl;
    //TODO: private List<String> roles;
}
