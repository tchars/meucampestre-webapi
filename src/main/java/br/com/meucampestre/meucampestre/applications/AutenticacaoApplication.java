package br.com.meucampestre.meucampestre.applications;

import br.com.meucampestre.meucampestre.domain.constants.TiposDePapeis;
import br.com.meucampestre.meucampestre.domain.models.Papel;
import br.com.meucampestre.meucampestre.domain.models.Usuario;
import br.com.meucampestre.meucampestre.domain.models.UsuarioPapelCondominio;
import br.com.meucampestre.meucampestre.repositories.UsuarioPapelCondominioRepo;
import br.com.meucampestre.meucampestre.services.JWTService;
import br.com.meucampestre.meucampestre.services.PapelService;
import br.com.meucampestre.meucampestre.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Service
@RequiredArgsConstructor
public class AutenticacaoApplication {

    private final JWTService _jwtService;

    private final UsuarioService _usuarioService;
    private final UsuarioPapelCondominioRepo _usuarioPapelCondominioRepo;
    private final PapelService _papelService;


    // TODO: MODIFICAR AUTH PARA CONSIDERAR ROLE DO CONDOMINIO ESPECÍFICO
    // Verifica se usuário que realizou request poderá realizar aquela request
    public void autenticarUsuario(String documentoUsuario, Long idCondominio,
                                     String papelEsperado) {

        Usuario usr = _usuarioService.buscarUsuarioPeloDocumento(documentoUsuario);

        if (usr == null)
        {
            throw new RuntimeException("Usuário do token não existe na base");
        }

        Papel papel = _papelService.buscarPapelPeloNome(papelEsperado);

        if (papel == null)
        {
            throw new RuntimeException("Papel do token não existe na base");
        }

        UsuarioPapelCondominio link =
                _usuarioPapelCondominioRepo.buscarPorUsuarioCondominioPapel(usr.getId(),
                        idCondominio, papel.getId());

        if (usr.getId() > 1)
        {
            if (link == null)
            {
                throw new RuntimeException("Você não tem permissão para este recurso");
            }

            if (!link.getPapel().getNome().equals(TiposDePapeis.SINDICO))
            {
                throw new RuntimeException("Você não tem permissão para esta ação");
            }
        }
    }

    // Renovar token
    public Boolean renovarToken(HttpServletRequest request,
                                HttpServletResponse response) throws IOException {

        String authorizationHeader = request.getHeader(AUTHORIZATION);

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer "))
        {
            return _jwtService.gerarRefreshToken(request, response, authorizationHeader);
        }

        return false;
    }
}
