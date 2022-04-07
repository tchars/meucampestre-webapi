package br.com.meucampestre.webapi.dto.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErroGenericoDTO {

    private Date dataErro;

    private String mensagem;

    private String detalhe;

}
