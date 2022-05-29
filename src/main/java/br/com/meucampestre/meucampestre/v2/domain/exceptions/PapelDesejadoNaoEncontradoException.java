package br.com.meucampestre.meucampestre.v2.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PapelDesejadoNaoEncontradoException extends RuntimeException {

    public PapelDesejadoNaoEncontradoException(String papel) {
        super("Papel desejado: " + papel + " não encontrado");
    }
}
