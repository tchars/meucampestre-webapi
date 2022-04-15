package br.com.meucampestre.webapi.controllers;

import br.com.meucampestre.webapi.entities.Usuario;
import br.com.meucampestre.webapi.exceptions.NotFoundException;
import br.com.meucampestre.webapi.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

public class BaseController {

//    @Autowired
//    private JwtService jwtService;

    @Autowired
    private UsuarioService usersService;

    public Usuario getUserByRequest(HttpServletRequest httpServletRequest) {
        String authorization = httpServletRequest.getHeader("Authorization");

        if (authorization != null && authorization.startsWith("Bearer")) {
            String token = authorization.split(" ")[1];
//            boolean isValid = jwtService.validateToken(token);
//
//            if (isValid) {
//                String loginUsuario = jwtService.getUsernameFromToken(token);
//                return usersService.buscarUsuarioPorEmail(loginUsuario);
//            }
        }

        throw new NotFoundException();
    }
}
