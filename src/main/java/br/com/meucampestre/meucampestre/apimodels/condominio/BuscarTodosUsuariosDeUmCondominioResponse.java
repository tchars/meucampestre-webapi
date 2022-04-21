package br.com.meucampestre.meucampestre.apimodels.condominio;

import br.com.meucampestre.meucampestre.apimodels.condominio.partials.MoradorDoCondominio;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BuscarTodosUsuariosDeUmCondominioResponse {

    Long id;
    String nome;
    String documento;
    Collection<MoradorDoCondominio> moradores = new ArrayList<>();
}
