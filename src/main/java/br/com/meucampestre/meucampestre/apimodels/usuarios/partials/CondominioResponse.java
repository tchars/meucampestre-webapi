package br.com.meucampestre.meucampestre.apimodels.usuarios.partials;

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
public class CondominioResponse
{
    private Long id;
    private String nome;
    private String documento;
    private Collection<String> tipoDePerfil;
}
