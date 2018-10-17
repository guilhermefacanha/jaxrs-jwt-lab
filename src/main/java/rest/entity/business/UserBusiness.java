package rest.entity.business;

import javax.ejb.Stateless;
import javax.inject.Inject;

import core.business.BusinessBase;
import core.dao.DAOBase;
import rest.entity.User;
import rest.entity.dao.UserDao;

@Stateless
public class UserBusiness extends BusinessBase<User> {

	private static final long serialVersionUID = 5148889294860883230L;

	@Inject
	private UserDao dao;
	
	@Override
	public DAOBase<User> getDao() {
		return dao;
	}

}
