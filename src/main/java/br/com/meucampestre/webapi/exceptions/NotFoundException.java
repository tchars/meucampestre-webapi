package br.com.meucampestre.webapi.exceptions;

public class NotFoundException extends RuntimeException  {

    public NotFoundException() {
        super("Não encontrado");
    }

    public NotFoundException(String message) {
        super(message);
    }
}
