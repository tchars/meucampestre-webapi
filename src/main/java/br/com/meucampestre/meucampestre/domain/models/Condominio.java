package br.com.meucampestre.meucampestre.domain.models;

import br.com.meucampestre.meucampestre.v2.domain.models.partials.UsuarioPapelCondominioPartial;
import lombok.*;
import org.springframework.lang.Nullable;

import javax.persistence.*;
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

    private String nome;

    @Nullable
    private String descricao;

    private String email;

    @Column(unique = true)
    private String documento;

    @Nullable
    private String endereco;

    private String imagemUrl;

    private Boolean ativo = true;

//    @Transient
//    private Collection<Usuario> usuarios = new ArrayList<>();

    @Transient
    private List<UsuarioPapelCondominioPartial> usuarios = new ArrayList<>();

    @Transient
    private List<Unidade> unidades = new ArrayList<>();
}