package br.com.meucampestre.webapi.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@Setter
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class EmailJaUtilizadoException extends RuntimeException {

    private String _nomeDoRecurso;
    private String _valorDoCampo;

    public EmailJaUtilizadoException(String nomeDoRecurso, String valorDoCampo) {

        super(nomeDoRecurso + " - " + valorDoCampo + " já utilizado.");

        _nomeDoRecurso = nomeDoRecurso;
        _valorDoCampo = valorDoCampo;
    }
}
