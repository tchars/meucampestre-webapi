package br.com.meucampestre.webapi.dto.autenticacao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CriarNovoUsuarioDTO {

    private Long id;

    private String nome;

    private String email;

    private String documento;
}
