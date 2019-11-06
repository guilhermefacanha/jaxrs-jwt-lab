package rest.manager.filter;

import java.io.IOException;
import java.security.Key;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import rest.manager.annotations.JWTTokenNeeded;
import rest.util.KeyGenerator;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */
@Slf4j
@Provider
@JWTTokenNeeded
@Priority(Priorities.AUTHENTICATION)
public class JWTTokenNeededFilter implements ContainerRequestFilter {

    // ======================================
    // =          Injection Points          =
    // ======================================

    @Inject
    private KeyGenerator keyGenerator;

    // ======================================
    // =          Business methods          =
    // ======================================

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        // Get the HTTP Authorization header from the request
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        log.info("#### authorizationHeader : " + authorizationHeader);

        // Check if the HTTP Authorization header is present and formatted correctly
        if (authorizationHeader == null || authorizationHeader.isEmpty()) {
            log.error("#### invalid authorizationHeader : " + authorizationHeader);
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("Authorization header must be provided").build());
            return;
        }

        // Extract the token from the HTTP Authorization header
        String token = authorizationHeader.trim();

        try {

            // Validate the token
            Key key = keyGenerator.generateKey();
            Jwts.parser().setSigningKey(key).parseClaimsJws(token);
            log.info("#### valid token : " + token);

        } catch (Exception e) {
            log.error("#### invalid token : " + token);
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }
}