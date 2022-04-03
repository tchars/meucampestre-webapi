package br.com.meucampestre.webapi.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@Setter
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class RecursoNaoEncontradoException extends RuntimeException {

    private String _nomeDoRecurso;
    private String _nomeDoCampo;
    private Long _valorDoCampo;

    public RecursoNaoEncontradoException(String nomeDoRecurso, String nomeDoCampo, Long valorDoCampo) {

        super(nomeDoRecurso + " não encontrado - " + nomeDoCampo + " : " + valorDoCampo);

        _nomeDoRecurso = nomeDoRecurso;
        _nomeDoCampo = nomeDoCampo;
        _valorDoCampo = valorDoCampo;
    }
}
