package br.com.meucampestre.meucampestre.v2.apiModels.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdicionarPapelAoUsuarioRequest
{
    private long idUsuario;
    //private List<String> nomesPapeis;
    private String nomePapel;
    private long idCondominio;
    private boolean tipoEspecial;
}
