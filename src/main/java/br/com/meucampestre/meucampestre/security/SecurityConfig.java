package br.com.meucampestre.meucampestre.security;

import br.com.meucampestre.meucampestre.domain.constants.Rotas;
import br.com.meucampestre.meucampestre.domain.constants.TiposDePapeis;
import br.com.meucampestre.meucampestre.security.filters.CustomAuthenticationFilter;
import br.com.meucampestre.meucampestre.security.filters.CustomAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService _userDetailsService;
    private final BCryptPasswordEncoder _passwordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(_userDetailsService).passwordEncoder(_passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // ALTERANDO A ROTA DE LOGIN
        CustomAuthenticationFilter customAuthenticationFilter =
                new CustomAuthenticationFilter(authenticationManagerBean());

        // Redefino a rota de autenticação do spring security
        customAuthenticationFilter.setFilterProcessesUrl(Rotas.URL_PREFIX_V1 + "/autenticacao");

        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(STATELESS);

        // Rota de Autenticação / LOGIN
        http.authorizeRequests().antMatchers(Rotas.URL_PREFIX_V1 + "/autenticacao/**")
                .permitAll();

        // BACKOFFICE - Super admin do sistema
        http.authorizeRequests()
                .antMatchers(Rotas.URL_PREFIX_V1 + "/backoffice/**")
                .hasAnyAuthority(TiposDePapeis.BACKOFFICE);

        // USUARIO

        http.authorizeRequests().anyRequest().authenticated();

        http.addFilter(customAuthenticationFilter);
        http.addFilterBefore(new CustomAuthorizationFilter(),
                UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
