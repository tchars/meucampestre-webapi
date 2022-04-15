package br.com.meucampestre.webapi.controllers;

import br.com.meucampestre.webapi.config.TokenProvider;
import br.com.meucampestre.webapi.dto.LoginDTO;
import br.com.meucampestre.webapi.dto.usuario.UsuarioDTO;
import br.com.meucampestre.webapi.entities.Usuario;
import br.com.meucampestre.webapi.model.AuthToken;
import br.com.meucampestre.webapi.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenProvider jwtTokenUtil;

    @Autowired
    private UsuarioService userService;

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> generateToken(@RequestBody LoginDTO loginUser) throws AuthenticationException {

        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUser.getEmail(),
                        loginUser.getSenha()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token = jwtTokenUtil.generateToken(authentication);
        return ResponseEntity.ok(new AuthToken(token));
    }

    @RequestMapping(value="/register", method = RequestMethod.POST)
    public Usuario saveUser(@RequestBody UsuarioDTO user) {
        return userService.save(user);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value="/adminping", method = RequestMethod.GET)
    public String adminPing() {
        return "Only Admins Can Read This";
    }

    @PreAuthorize("hasRole('SINDICO')")
    @RequestMapping(value="/userping2", method = RequestMethod.GET)
    public String userPing2() {
        return "Sindico User Can Read This 2 ";
    }

    @PreAuthorize("hasRole('ROLE_SINDICO')")
    @RequestMapping(value="/userping", method = RequestMethod.GET)
    public String userPing() {
        return "Sindico User Can Read This";
    }

    @RequestMapping(value="/ping", method = RequestMethod.GET)
    public String ping() {
        return "Any User Can Read This";
    }

}
