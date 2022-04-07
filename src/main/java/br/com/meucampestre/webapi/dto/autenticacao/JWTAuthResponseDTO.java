package br.com.meucampestre.webapi.dto.autenticacao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JWTAuthResponseDTO {

    private String tokenDeAcesso;
    private String tipoDeToken = "Bearer";

    public JWTAuthResponseDTO(String tokenDeAcesso) {
        this.tokenDeAcesso = tokenDeAcesso;
    }
}
