package br.com.meucampestre.meucampestre.apimodels.usuarios;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CriarUsuarioRequestV2
{
    private String nome;
    private String email;
    private String senha;
    private String documento;
    private String telefone;
    private List<String> papeis;
    private List<Long> unidades;
}
