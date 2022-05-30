package br.com.meucampestre.meucampestre.domain.models;

import br.com.meucampestre.meucampestre.v2.domain.models.partials.UsuarioPapelCondominioPartial;
import lombok.*;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Condominio
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column()
    private String descricao;

    @Column()
    private String email;

    @Column(unique = true, nullable = false)
    private String documento;

    @Column()
    private String endereco;

    @Column()
    private String imagemUrl;

    @Column(nullable = false)
    private Boolean ativo = true;

//    @Transient
//    private Collection<Usuario> usuarios = new ArrayList<>();

    @Transient
    private List<UsuarioPapelCondominioPartial> usuarios = new ArrayList<>();

    @Transient
    private List<Unidade> unidades = new ArrayList<>();
}