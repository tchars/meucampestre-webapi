package br.com.meucampestre.meucampestre.apimodels.usuarios;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CriarUsuarioRequest {

    private String nome;
    private String senha;
    private String email;
    private String documento;
    private String papel;
}
