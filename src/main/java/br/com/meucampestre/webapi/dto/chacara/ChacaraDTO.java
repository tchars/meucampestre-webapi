package br.com.meucampestre.webapi.dto.chacara;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChacaraDTO {

    private Long id;

    @NotNull
    @Range(min = 1, max = 100000)
    private Long idCondominio;

    private String identificador;

    @NotEmpty
    @NotNull
    @Size(min = 3)
    private String titulo;

    private String descricao;

    private String observacao;

    private Date criadoEm;
}
