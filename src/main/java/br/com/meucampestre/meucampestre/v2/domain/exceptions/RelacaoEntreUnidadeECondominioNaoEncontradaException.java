package br.com.meucampestre.meucampestre.v2.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RelacaoEntreUnidadeECondominioNaoEncontradaException extends RuntimeException {

    public RelacaoEntreUnidadeECondominioNaoEncontradaException(long idUnidade, long idCondominio) {
        super("Unidade id " + idUnidade + " não encontrada para o condomínio id " + idCondominio);
    }
}
