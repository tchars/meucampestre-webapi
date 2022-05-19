package br.com.meucampestre.meucampestre.apimodels.condominio;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CriarCondominioRequest
{
    private String nome;
    private String descricao;
    private String email;
    private String documento;
    private String endereco;
    private String imagemUrl;
}
