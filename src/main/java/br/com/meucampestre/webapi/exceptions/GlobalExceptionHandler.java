package br.com.meucampestre.webapi.exceptions;

import br.com.meucampestre.webapi.dto.exceptions.ErroGenerico;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroGenerico> tratarErroDeRecursoNaoEncontrado(RecursoNaoEncontradoException exception, WebRequest webRequest)
    {
        ErroGenerico erroGerado = new ErroGenerico(new Date(), exception.getMessage(), webRequest.getDescription(false));
        return new ResponseEntity<>(erroGerado, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroGenerico> tratarErroDeServidor(Exception exception, WebRequest webRequest)
    {
        String mensagem = exception.getMessage() == null ? "Erro interno do servidor" : exception.getMessage();

        ErroGenerico erroGerado = new ErroGenerico(new Date(), mensagem, webRequest.getDescription(false));
        return new ResponseEntity<>(erroGerado, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, String> erros = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {

            String nomeCampo = ((FieldError)error).getField();
            String mensagem = error.getDefaultMessage();

            erros.put(nomeCampo, mensagem);
        });

        return new ResponseEntity<>(erros, HttpStatus.BAD_REQUEST);
    }
}
