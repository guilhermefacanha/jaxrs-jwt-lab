package rest.manager;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Provider
@Produces(MediaType.APPLICATION_JSON)
public class JacksonConfig implements ContextResolver<ObjectMapper>
{
	final ObjectMapper objectMapper;

	  public JacksonConfig() {
	    objectMapper = new ObjectMapper();
	    /* We want dates to be treated as ISO8601 not timestamps. */
	    objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
	  }

	  @Override
	  public ObjectMapper getContext(Class<?> arg0) {
	    return objectMapper;
	  }
}