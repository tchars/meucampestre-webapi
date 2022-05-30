package br.com.meucampestre.meucampestre.v2.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UnidadeNaoEncontradaException extends RuntimeException {

    public UnidadeNaoEncontradaException(long idCondominio) {
        super("Unidade " + idCondominio + " não encontrada");
    }
}
