package br.com.meucampestre.meucampestre.apimodels.usuarios;

import br.com.meucampestre.meucampestre.apimodels.usuarios.partials.CondominioResponse;
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
public class BuscarDadosDoPerfilResponse
{
    private Long id;
    private String nome;
    private String documento;
    private Collection<CondominioResponse> condominios = new ArrayList<>();
}

