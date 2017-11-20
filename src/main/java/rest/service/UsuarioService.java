package rest.service;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import static javax.ws.rs.core.HttpHeaders.*;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import rest.manager.binding.JWTTokenNeeded;
import rest.util.KeyGenerator;

@Slf4j
@Path(value = "/usuarios")
public class UsuarioService {

	private final String erroGET = "metodo get nao permitido";
	private static List<String> lista = getListaInicial();

	@Context
	private UriInfo uriInfo;

	@Inject
	private KeyGenerator keyGenerator;

	@GET
	@Path("/")
	public Response getUsuarios() {
		return Response.ok(lista.toString()).build();
	}

	@GET
	@Path("/{code}")
	public Response getUsuario(@PathParam("code") int code) {
		if (lista.size() > code)
			return Response.ok(lista.get(code)).build();
		else
			return Response.status(Response.Status.NOT_FOUND).entity("NOT FOUND").build();
	}

	@GET
	@Path("/new")
	public Response novoUsuario() {
		return getError();
	}

	@JWTTokenNeeded
	@POST
	@Path("/new")
	public Response novoUsuario(@FormParam("user") String user) {
		if (user == null || user.isEmpty())
			return Response.serverError().entity(new String("campo obrigatorio - user")).build();
		System.out.println("Novo usuário => ".concat(user));
		lista.add(user);
		return Response.ok("Usuario cadastrado com sucesso").build();
	}

	@GET
	@Path("/delete")
	public Response removerUsuario() {
		return getError();

	}

	private Response getError() {
		return Response.serverError().entity(erroGET).build();
	}

	@POST
	@Path("/delete")
	public Response removerUsuario(@FormParam("code") int code) {
		if (lista.size() > code) {
			lista.remove(code);
			return Response.ok("Usuario removido com sucesso").build();
		} else
			return Response.serverError().entity(new String("NOT FOUND")).build();

	}

	@GET
	@Path("/auth")
	public Response autenticarUsuario() {
		return getError();
	}
	
	@POST
	@Path("/auth")
	public Response autenticarUsuario(@FormParam("user") String user, @FormParam("pass") String pass) {
		if (user != null && !user.isEmpty() && pass != null && !pass.isEmpty()) {
			if ("admin".equals(user) && "admin".equals(pass)) {
				Key key = keyGenerator.generateKey();
				String jwtToken = Jwts.builder().setSubject(user).setIssuer(uriInfo.getAbsolutePath().toString())
						.setIssuedAt(new Date()).setExpiration(toDate(LocalDateTime.now().plusMinutes(15L)))
						.signWith(SignatureAlgorithm.HS512, key).compact();
				log.info("#### generating token for a key : " + jwtToken + " - " + key);
				return Response.ok("Usuario " + user + " autenticado com sucesso").header( AUTHORIZATION, jwtToken).build();
			} else
				return Response.serverError().entity(new String("Usuario ou senha invalidos")).build();
			
		} else
			return Response.serverError().entity(new String("PARAMETROS INCOMPLETOS")).build();

	}

	// ======================================
	// = Private methods =
	// ======================================

	private Date toDate(LocalDateTime localDateTime) {
		return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
	}

	private static List<String> getListaInicial() {
		lista = new ArrayList<>();
		lista.add("USER 1");
		lista.add("USER 2");
		lista.add("USER 3");
		lista.add("USER 4");
		return lista;
	}
}
