package br.com.meucampestre.meucampestre.domain.models;

import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Data
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

    @Transient
    private Collection<Usuario> usuarios = new ArrayList<>();

    @Transient
    private List<Unidade> unidades = new ArrayList<>();
}
