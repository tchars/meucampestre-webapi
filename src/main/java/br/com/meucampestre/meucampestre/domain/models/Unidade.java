package br.com.meucampestre.meucampestre.domain.models;

import br.com.meucampestre.meucampestre.v2.domain.models.partials.UsuarioUnidadePartial;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Unidade
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = true)
    private String endereco;

    @Column(nullable = true)
    private String descricao;

    @Column(columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Basic(optional = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date criadoEm;

    @Column(columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Basic(optional = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date atualizadoEm;

    @Transient
    private Collection<UsuarioUnidadePartial> usuarios = new ArrayList<>();
}
