package br.com.meucampestre.meucampestre.v2.domain.models.partials;

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
public class UsuarioPapelCondominioPartial {

    private long id;
    private String nome;
    private String documento;

    private List<String> papeis = new ArrayList<>();
}