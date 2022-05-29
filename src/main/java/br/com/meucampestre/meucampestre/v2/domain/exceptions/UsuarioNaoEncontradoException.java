package br.com.meucampestre.meucampestre.v2.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UsuarioNaoEncontradoException extends RuntimeException {

    public UsuarioNaoEncontradoException(long id) {
        super("Usuário " + id + " não encontrado");
    }

    public UsuarioNaoEncontradoException(String documento) {
        super("Usuário documento: " + documento + " não encontrado");
    }
}
