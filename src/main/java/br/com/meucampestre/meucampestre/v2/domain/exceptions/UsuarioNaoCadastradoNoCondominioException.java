package br.com.meucampestre.meucampestre.v2.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UsuarioNaoCadastradoNoCondominioException extends RuntimeException {

    public UsuarioNaoCadastradoNoCondominioException(String documento) {
        super("Usuário com documento " + documento + " não possui acesso nesse condominio.");
    }
}
