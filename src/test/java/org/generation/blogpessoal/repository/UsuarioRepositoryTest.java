package org.generation.blogpessoal.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.generation.blogpessoal.model.Usuario;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;



@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UsuarioRepositoryTest {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@BeforeAll
	void start() {
		usuarioRepository.save(new Usuario(0L,"DJ Cleiton Rasta", "Cleitinho@pedra.com","cabecadegelo","https://imagens.ne10.uol.com.br/veiculos/_midias/jpg/2020/07/10/806x444/1_dj_cleiton_rasta_perfil_body_image_1474918939-16274795.jpg\r\n"
				+ ""));
		usuarioRepository.save(new Usuario(0L,"DJ rata Rasta", "rata@pedra.com","gelos","https://imagens.ne10.uol.com.br/veiculos/_midias/jpg/2020/07/10/806x444/1_dj_cleiton_rasta_perfil_body_image_1474918939-16274795.jpg\r\n"
				+ ""));
	}
	@Test
	@DisplayName("retorna apenas um usuario")
	public void deveRetornarUmUsuario() {
		Optional<Usuario> usuario = usuarioRepository.findByUsuario("Cleitinho@pedra.com");
		assertTrue(usuario.get().getUsuario().equals("Cleitinho@pedra.com"));

	}
	@Test
	@DisplayName("retorna apenas dois usuario")
	public void deveRetornarDoisUsuario() {
		List<Usuario> listaDeUsuario = usuarioRepository.findAllByNomeContainingIgnoreCase("DJ");
		assertEquals(2, listaDeUsuario.size());
	
	   assertTrue(listaDeUsuario.get(0).getNome().equals("DJ Cleiton Rasta"));
	}
}
