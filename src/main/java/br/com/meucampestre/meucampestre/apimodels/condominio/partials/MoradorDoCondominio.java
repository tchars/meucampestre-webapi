package br.com.meucampestre.meucampestre.apimodels.condominio.partials;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MoradorDoCondominio {
    private Long id;
    private String nome;
    private String documento;
    private Collection<String> tipoDePerfil;
    private String fotoDePerfil;
}
