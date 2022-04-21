package br.com.meucampestre.meucampestre.apimodels.usuarios;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AtualizarUsuarioResponse
{
    private String nome;
    private String email;
    private String telefone;

    private Collection<String> nivelDeAcesso = new ArrayList<>();
}
