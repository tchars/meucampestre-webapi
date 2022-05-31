package br.com.meucampestre.meucampestre.v2.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RelacaoEntreUnidadeEUsuarioJaExisteException extends RuntimeException {

    public RelacaoEntreUnidadeEUsuarioJaExisteException(String documentoUsuario,
                                                        long idUnidade) {
        super("Usuário documento " + documentoUsuario + " já faz parte da unidade id " + idUnidade);
    }
}
