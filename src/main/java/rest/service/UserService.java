package rest.service;

import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.github.javafaker.Faker;

import core.business.BusinessBase;
import core.service.ServiceBase;
import core.service.entity.JsonReturn;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import rest.entity.User;
import rest.entity.business.UserBusiness;
import rest.util.KeyGenerator;

@Slf4j
@Api(value = "User Service Endpoint")
@Path(value = "/users")
public class UserService extends ServiceBase<User> {

	private static final long serialVersionUID = -6648035204151594141L;

	@Inject
	private UserBusiness userBusiness;
	@Inject
	private KeyGenerator keyGenerator;
	@Context
	private UriInfo uriInfo;
	
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
				String jwtToken = Jwts.builder()
						.setClaims(getCustomClaims())
						.setSubject(user).setIssuer(uriInfo.getAbsolutePath().toString())
						.setIssuedAt(new Date()).setExpiration(toDate(LocalDateTime.now().plusMinutes(15L)))
						.signWith(SignatureAlgorithm.HS512, key).compact();
				log.info("#### generating token for a key : " + jwtToken + " - " + key);
				return Response.ok(JsonReturn.builder().message("User authenticated!").build()).header( AUTHORIZATION, jwtToken).build();
			} else
				return setError("User invalid!", "", "Authentication");
			
		} else
			return setError("Body incomplete", "", "Authentication");

	}
	
	private Map<String, Object> getCustomClaims() {
		Map<String, Object> map = new HashMap<>();
		map.put("profile", "admin_profile");
		map.put("department", "HR");
		return map;
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/save")
	@Override
	public Response save(User entidade) {
		Faker faker = new Faker();
		entidade.setCity(faker.gameOfThrones().city());
		String firstName = faker.name().firstName();
		entidade.setName(firstName);
		entidade.setEmail(firstName.trim().replace(" ", ".") + "@gameofthrones.com");
		entidade.setPhone(faker.phoneNumber().cellPhone());
		return super.save(entidade);
	}

	@Override
	public BusinessBase<User> getBusiness() {
		return userBusiness;
	}
	
	private Date toDate(LocalDateTime localDateTime) {
		return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
	}

}
