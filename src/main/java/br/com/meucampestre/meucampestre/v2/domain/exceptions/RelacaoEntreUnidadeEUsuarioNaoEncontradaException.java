package br.com.meucampestre.meucampestre.v2.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RelacaoEntreUnidadeEUsuarioNaoEncontradaException extends RuntimeException {

    public RelacaoEntreUnidadeEUsuarioNaoEncontradaException(String documentoUsuario,
                                                             long idUnidade) {
        super("Usuário documento " + documentoUsuario + " não encontrada para a unidade id " + idUnidade);
    }
}
