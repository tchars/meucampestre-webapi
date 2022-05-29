package br.com.meucampestre.meucampestre.v2.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CondominioJaCadastradoException extends RuntimeException {

    public CondominioJaCadastradoException(String documentoCondominio) {
        super("Condomínio com documento " + documentoCondominio + " já existe.");
    }
}
