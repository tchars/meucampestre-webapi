package br.com.meucampestre.webapi.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final JWTTokenProvider _jwtTokenProvider;
    private final CustomUserDetailsService _customUserDetailsService;

    @Autowired
    public JWTAuthenticationFilter(JWTTokenProvider jwtTokenProvider, CustomUserDetailsService customUserDetailsService) {
        _jwtTokenProvider = jwtTokenProvider;
        _customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Obtemos o token pela solicitação http
        String token = obterJWTdaRequisicao(request);

        // Validamos o token
        if(StringUtils.hasText(token) && _jwtTokenProvider.validarToken(token))
        {
            // obtemos o email do token
            String username = _jwtTokenProvider.obterEmailPeloJWT(token);

            // carregamos os dados do usuários baseado no email
            UserDetails userDetails = _customUserDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null,userDetails.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // estabelecemos a autenticação
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        filterChain.doFilter(request, response);
    }

    //Bearer token de acceso
    private String obterJWTdaRequisicao(HttpServletRequest request)
    {
        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer"))
        {
            return bearerToken.substring(7, bearerToken.length());
        }

        return null;
    }
}
