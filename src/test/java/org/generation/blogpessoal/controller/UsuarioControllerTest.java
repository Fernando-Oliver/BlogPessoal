package org.generation.blogpessoal.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.generation.blogpessoal.model.Usuario;
import org.generation.blogpessoal.service.UsuarioService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UsuarioControllerTest {

	@Autowired
	private UsuarioService usuarioService;

	@Autowired // usa somente para test para conseguir usar os metodos http
	private TestRestTemplate testRestTemplate;

	@Test
	@Order(1)
	@DisplayName("Cadastrar apenas um usuário")
	public void deveCadastrarUmUsuario() {

		HttpEntity<Usuario> requisicao = new HttpEntity<Usuario>(
				new Usuario(0L, "Jose", "jose@imperiobronze.com", "trabalholindo", "https://i.imgur.com/FETvs2O.jpg"));

		ResponseEntity<Usuario> resposta = testRestTemplate.exchange("/usuarios/cadastrar", HttpMethod.POST, requisicao,
				Usuario.class);

		assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
		assertEquals(requisicao.getBody().getNome(), resposta.getBody().getNome());
		assertEquals(requisicao.getBody().getUsuario(), resposta.getBody().getUsuario());
		
	}
	
	@Test
	@Order(2)
	@DisplayName("Não deve permitir duplicação do Usuário")
	public void naoDeveDuplicarUsuario() {
		usuarioService.cadastrarUsuario(new Usuario(0L,"Maria da Silva", "maria@gmail.com","12345678","" ));
		
		HttpEntity<Usuario> requisicao = new HttpEntity<Usuario>(new Usuario(0L,"Maria da Silva", "maria@gmail.com","12345678",""));
		
		ResponseEntity<Usuario> resposta = testRestTemplate.exchange("/usuarios/cadastrar", HttpMethod.POST, requisicao, Usuario.class);
		
		assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
	}
	
	@Test
	@Order(3)
	@DisplayName("alterar um usuario")
	public void deveAtualizarUmUsuario() {
		
		Optional<Usuario> usuarioCreate = usuarioService.cadastrarUsuario(new Usuario(0L,"Juliana Andreas","juliana@gmail.com","1234567",""));
		
		Usuario usuarioUpdate = new Usuario(usuarioCreate.get().getId(),"Juliana Ramos", "juliana@gmail.com","juliana1","");
		
		HttpEntity<Usuario> requisicao = new HttpEntity<Usuario>(usuarioUpdate);
		
		ResponseEntity<Usuario> resposta = testRestTemplate
				.withBasicAuth("root","6478")
				.exchange("/usuarios/atualizar", HttpMethod.PUT, requisicao, Usuario.class);
		
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
		assertEquals(usuarioUpdate.getNome(), resposta.getBody().getNome());
		assertEquals(usuarioUpdate.getUsuario(), resposta.getBody().getUsuario());
		
	}

	@Test
	@Order(4)
	@DisplayName("Lista de Usuários")
	public void deveMostrarTodosUsuarios() {
		usuarioService.cadastrarUsuario(new Usuario(0L,"sabrina", "sabrina@gmail.com", "sabrina123",""));
		
		usuarioService.cadastrarUsuario(new Usuario(0L,"Ricardo", "ricardo@gmail.com", "ricardo3",""));
		
		ResponseEntity<String> resposta = testRestTemplate
				.withBasicAuth("root", "6478")
				.exchange("/usuarios/all", HttpMethod.GET, null, String.class);
		
		assertEquals(HttpStatus.OK, resposta.getStatusCode());

		
	}
	
}
