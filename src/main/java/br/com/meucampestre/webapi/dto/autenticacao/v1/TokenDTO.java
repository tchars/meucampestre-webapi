package br.com.meucampestre.webapi.dto.autenticacao.v1;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TokenDTO {
    private String email;
    private String token;
}
