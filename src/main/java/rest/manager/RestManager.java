package rest.manager;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.reflections.Reflections;

import core.service.ServiceBase;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;
import rest.manager.filter.JWTTokenNeededFilter;

@ApplicationPath(value = "rest")
public class RestManager extends Application {
	public RestManager() {
		BeanConfig conf = new BeanConfig();
		conf.setTitle("LAB REST API");
		conf.setDescription("Api Rest for Lab Projects");
		conf.setVersion("0.0.1");
		conf.setHost("localhost:8080");
		conf.setBasePath("/restjwt/rest");
		conf.setSchemes(new String[] { "http" });
		conf.setResourcePackage("rest.service");
		conf.setScan(true);
	}

	@Override
	public Set<Class<?>> getClasses() {

		Reflections reflections = new Reflections("rest.service");

		@SuppressWarnings("rawtypes")
		Set<Class<? extends ServiceBase>> services = reflections.getSubTypesOf(ServiceBase.class);

		Set<Class<?>> resources = new HashSet<>();
		resources.addAll(services);

		// add filters
		resources.add(JWTTokenNeededFilter.class);

		// classes do swagger...
		resources.add(ApiListingResource.class);
		resources.add(SwaggerSerializers.class);
		return resources;
	}
}
