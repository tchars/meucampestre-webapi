package br.com.meucampestre.meucampestre.domain.models;

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
    private String titulo;
    private String endereco;
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
    private Collection<Usuario> usuarios = new ArrayList<>();
}
