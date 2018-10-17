package rest.entity.dao;

import javax.ejb.Stateless;

import core.dao.DAOBase;
import rest.entity.User;

@Stateless
public class UserDao extends DAOBase<User> {
	private static final long serialVersionUID = -7633628269401793747L;

}
