package br.com.meucampestre.meucampestre.v2.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UsuarioJaCadastradoComRoleParaOCondominioEscolhidoException extends RuntimeException {

    public UsuarioJaCadastradoComRoleParaOCondominioEscolhidoException(String documento,
                                                                       String papel,
                                                                       String condominio)
    {
        super("Usuário id: " + documento + " já possui o papel de: " + papel + " para o condomínio: " + condominio);
    }
}
