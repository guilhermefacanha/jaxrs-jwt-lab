package rest.manager.exceptions;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

@Provider
public class Mapper implements ExceptionMapper<Exception> {

	@Override
	public Response toResponse(Exception ex) {

		JsonObject json = new JsonObject();
		json.addProperty("status", Status.BAD_REQUEST.getStatusCode());
		json.addProperty("error", Status.BAD_REQUEST.getReasonPhrase());
		json.addProperty("message", ex.getMessage());
		json.add("exception", new Gson().toJsonTree(ExceptionUtils.getStackFrames(ex)));
		
		return Response.status(Status.BAD_REQUEST).entity(json.toString()).header("Content-Type", MediaType.APPLICATION_JSON).build();
	}
}