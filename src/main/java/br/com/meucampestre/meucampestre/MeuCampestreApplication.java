package br.com.meucampestre.meucampestre;

import br.com.meucampestre.meucampestre.domain.constants.TiposDePapeis;
import br.com.meucampestre.meucampestre.domain.models.Papel;
import br.com.meucampestre.meucampestre.domain.models.Usuario;
import br.com.meucampestre.meucampestre.services.UsuarioService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Date;

@SpringBootApplication
public class MeuCampestreApplication {

	public static void main(String[] args) {
		SpringApplication.run(MeuCampestreApplication.class, args);
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	CommandLineRunner run(UsuarioService usuarioService){
		return args -> {
			usuarioService.salvarPapel(new Papel(null, TiposDePapeis.BACKOFFICE, null, null));
			usuarioService.salvarPapel(new Papel(null, TiposDePapeis.CONDOMINIO, null, null));
			usuarioService.salvarPapel(new Papel(null, TiposDePapeis.SINDICO, null, null));
			usuarioService.salvarPapel(new Papel(null, TiposDePapeis.CONSELHEIRO, null, null));
			usuarioService.salvarPapel(new Papel(null, TiposDePapeis.PORTEIRO, null, null));
			usuarioService.salvarPapel(new Papel(null, TiposDePapeis.MORADOR, null, null));

			usuarioService.salvarUsuario(
					new Usuario(null, "Backoffice MeuCampestre", "123456", "00011122233",
							new ArrayList<>(), null, null)
			);

			usuarioService.adicionarPapelAoUsuario("00011122233", TiposDePapeis.BACKOFFICE);
		};
	}
}
