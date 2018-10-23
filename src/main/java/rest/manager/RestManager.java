package rest.manager;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.jboss.resteasy.plugins.interceptors.CorsFilter;
import org.reflections.Reflections;

import core.service.ServiceBase;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;
import rest.manager.filter.CORSFilter;
import rest.manager.filter.JWTTokenNeededFilter;

@ApplicationPath(value = "rest")
public class RestManager extends Application {
	public RestManager() {
		BeanConfig conf = new BeanConfig();
		conf.setTitle("LAB REST API");
		conf.setDescription("Api Rest for Lab Projects");
		conf.setVersion("0.0.1");
		conf.setBasePath("/restjwt");
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
		resources.add(CORSFilter.class);

		// classes do swagger...
		resources.add(ApiListingResource.class);
		resources.add(SwaggerSerializers.class);
		return resources;
	}
	
	@Override
    public Set<Object> getSingletons() {
		Set<Object> singletons = new HashSet<Object>();
		CorsFilter corsFilter = new CorsFilter();
        corsFilter.getAllowedOrigins().add("*");
        corsFilter.setAllowedMethods("OPTIONS, GET, POST, DELETE, PUT, PATCH");
        singletons.add(corsFilter);
        
        return singletons;
    }
}
