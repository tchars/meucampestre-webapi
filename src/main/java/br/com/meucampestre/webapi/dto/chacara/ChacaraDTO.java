package br.com.meucampestre.webapi.dto.chacara;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChacaraDTO {

    private Long id;

    private Long idCondominio;

    private String identificador;

    private String titulo;

    private String descricao;

    private String observacao;

    private Date criadoEm;
}
