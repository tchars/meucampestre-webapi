package br.com.meucampestre.meucampestre.v2.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CondominioNaoEncontradoException extends RuntimeException {

    public CondominioNaoEncontradoException(long idCondominio) {
        super("Condomínio " + idCondominio + " não encontrado");
    }

    public CondominioNaoEncontradoException(String documentoCondominio) {
        super("Condomínio documento: " + documentoCondominio + " não encontrado");
    }
}
