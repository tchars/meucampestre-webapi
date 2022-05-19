package br.com.meucampestre.meucampestre.domain.models;

import lombok.*;
import org.springframework.lang.Nullable;

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
public class Usuario
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;

    @Column(unique = true)
    private String email;

    private String senha;

    @Column(unique = true)
    private String documento;

    @Nullable
    private String telefone;

    @Nullable
    private String imagemUrl;

    private Boolean ativo = true;

    @Column(columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Basic(optional = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date criadoEm;

    @Column(columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Basic(optional = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date atualizadoEm;

    @Transient
    private Collection<Papel> papeis = new ArrayList<>();
}
