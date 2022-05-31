package br.com.meucampestre.meucampestre.apimodels.condominio.partials;

import br.com.meucampestre.meucampestre.domain.models.Papel;
import br.com.meucampestre.meucampestre.domain.models.Usuario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MoradorDoCondominio {
    private Long id;
    private String nome;
    private String documento;
    private Collection<String> tipoDePerfil;
    private String fotoDePerfil;

    public MoradorDoCondominio(Usuario usuario) {
        this.id = usuario.getId();
        this.nome = usuario.getNome();
        this.documento = usuario.getDocumento();
        this.fotoDePerfil = usuario.getImagemUrl();

        for (Papel papel : usuario.getPapeis()) {
            this.tipoDePerfil.add(papel.getNome());
        }
    }
}
