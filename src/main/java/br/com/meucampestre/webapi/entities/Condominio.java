package br.com.meucampestre.webapi.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "condominios")
public class Condominio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome_condominio")
    private String nomeCondominio;

    @Column(name = "descricao")
    private String descricao;

    @JsonBackReference
    @OneToMany(mappedBy = "condominio", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Chacara> chacaras = new HashSet<>();

    @Column(name = "criado_em", insertable = false, updatable = false, columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Basic(optional = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date criadoEm;

    @Column(name = "atualizado_em", insertable = false, updatable = false, columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Basic(optional = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date atualizadoEm;
}
