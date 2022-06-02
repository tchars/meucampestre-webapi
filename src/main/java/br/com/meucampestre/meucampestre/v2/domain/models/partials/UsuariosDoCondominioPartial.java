package br.com.meucampestre.meucampestre.v2.domain.models.partials;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UsuariosDoCondominioPartial {

    private long id;
    private String nome;
    private String documento;
    private String imagemUrl;
}