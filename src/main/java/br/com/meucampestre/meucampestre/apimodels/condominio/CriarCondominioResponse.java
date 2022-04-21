package br.com.meucampestre.meucampestre.apimodels.condominio;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CriarCondominioResponse {
    private Long id;
    private String email;
    private String documento;
    private String descricao;
    private Date criadoEm;
}
