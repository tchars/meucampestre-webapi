package br.com.meucampestre.meucampestre.v2.apiModels.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AtualizarMeusDadosDaContaRequest {

    private String nome;

    private String email;

    private String senha;

    private String telefone;

    private String imagemUrl;
}
