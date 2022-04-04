package br.com.meucampestre.webapi.services;

import br.com.meucampestre.webapi.dto.usuario.UsuarioDTO;
import br.com.meucampestre.webapi.entities.Condominio;
import br.com.meucampestre.webapi.entities.Role;
import br.com.meucampestre.webapi.entities.Usuario;
import br.com.meucampestre.webapi.exceptions.DocumentoJaUtilizadoException;
import br.com.meucampestre.webapi.exceptions.EmailJaUtilizadoException;
import br.com.meucampestre.webapi.exceptions.RecursoNaoEncontradoException;
import br.com.meucampestre.webapi.repositories.IRoleRepository;
import br.com.meucampestre.webapi.repositories.IUsuarioRepository;
import br.com.meucampestre.webapi.services.interfaces.IUsuarioService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService implements IUsuarioService {

    private final IUsuarioRepository _usuarioRepository;
    private final IRoleRepository _roleRepository;
    private final ModelMapper _modelMapper;

    @Autowired
    public UsuarioService(IUsuarioRepository usuarioRepository, IRoleRepository roleRepository, ModelMapper modelMapper) {
        _usuarioRepository = usuarioRepository;
        _roleRepository = roleRepository;
        _modelMapper = modelMapper;
    }

    @Override
    public UsuarioDTO criarUsuario(UsuarioDTO usuarioDTO) {

        Usuario usuarioASerCriado = _modelMapper.map(usuarioDTO, Usuario.class);

        // Verifico se o email já está sendo usado
        VerificarSeEmailJaExiste(usuarioASerCriado.getEmail());

        // Verifico se alguém usou o cnpj / cpf
        VerificarSeDocumentoJaFoiUtilizado(usuarioASerCriado.getDocumento());


        Role roleSindico = _roleRepository.findById(1L).orElse(null);

        usuarioASerCriado.getRoles().add(roleSindico);

        Usuario usuarioCriado = _usuarioRepository.save(usuarioASerCriado);

        return _modelMapper.map(usuarioCriado, UsuarioDTO.class);
    }

    private void VerificarSeDocumentoJaFoiUtilizado(String documento)
    {
        if(_usuarioRepository.existsByDocumento(documento))
        {
            throw new DocumentoJaUtilizadoException(UsuarioService.class.toString(), documento);
        }
    }

    private void VerificarSeEmailJaExiste(String email)
    {
        if(_usuarioRepository.existsByEmail(email))
        {
            throw new EmailJaUtilizadoException(UsuarioService.class.toString(), email);
        }
    }
}
