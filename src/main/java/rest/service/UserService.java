package rest.service;

import javax.inject.Inject;
import javax.ws.rs.Path;

import core.business.BusinessBase;
import core.service.ServiceBase;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import rest.entity.User;
import rest.entity.business.UserBusiness;

@Slf4j
@Api(value = "User Service Endpoint")
@Path(value = "/users")
public class UserService extends ServiceBase<User> {

	private static final long serialVersionUID = -6648035204151594141L;

	@Inject
	private UserBusiness userBusiness;

	@Override
	public BusinessBase<User> getBusiness() {
		return userBusiness;
	}

}
