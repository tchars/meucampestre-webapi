package br.com.meucampestre.meucampestre.apimodels.autenticacao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AutenticarRequest {
    private String documento;
    private String senha;
}
