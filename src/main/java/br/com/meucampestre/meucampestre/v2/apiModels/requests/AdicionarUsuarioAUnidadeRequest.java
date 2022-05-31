package br.com.meucampestre.meucampestre.v2.apiModels.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdicionarUsuarioAUnidadeRequest
{
    private String documento;
}
