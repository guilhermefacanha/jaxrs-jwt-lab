package core.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import core.annotations.Transactional;
import core.entity.EntityBase;
import core.util.ModelUtils;

public class DAOBase<T extends EntityBase> implements Serializable {
	private static final long serialVersionUID = 7392952886823572130L;

	@Inject
	protected EntityManager manager;

	protected Class<T> modelClass;

	// Metodo responsavel por persistir um objeto no banco de dados
	@Transactional
	public void salvar(T objeto) throws Exception {
		// Verificacao se eh um update, caso seja update usar o merge para
		// conectar o objeto ao banco
		if (objeto.getId() > 0) {
			// editar objeto
			manager.merge(objeto);
		} else {
			// salvar objeto
			manager.persist(objeto);
		}
	}

	// Metodo responsavel por remover um objeto no banco de dados
	@Transactional
	public void remover(T objeto) throws Exception {
		String classe = getClasseEntidade().getSimpleName();

		// Verificacao se eh um update, caso seja update usar o merge para
		// conectar o objeto ao banco
		if (objeto.getId() > 0) {
			manager.createQuery("DELETE FROM " + classe + " x WHERE x.id=:id").setParameter("id", objeto.getId())
					.executeUpdate();
		}

	}
	@Transactional
	public void remover(Object id) throws Exception {
		String classe = getClasseEntidade().getSimpleName();

		manager.createQuery("DELETE FROM " + classe + " x WHERE x.id=:id").setParameter("id", id).executeUpdate();

	}

	// Metoto para conusltar todos os objetos
	@SuppressWarnings("unchecked")
	public List<T> getListaTodos() throws Exception {
		String classe = getClasseEntidade().getSimpleName();
		return manager.createQuery("SELECT x FROM " + classe + " x").getResultList();
	}

	public boolean isValidaManager(EntityManager entityManager) {
		if (this.manager == null)
			this.manager = entityManager;
		return manager != null;
	}

	@SuppressWarnings("unchecked")
	public List<T> getListaTodosPaginacao(String atributoOrdenacao, String dir, int comeco, int quantidade)
			throws Exception {
		Session session = (Session) manager.getDelegate();
		Criteria crit = session.createCriteria(getClasseEntidade());
		if (dir.equals("asc"))
			crit.addOrder(Order.asc(atributoOrdenacao));
		else
			crit.addOrder(Order.desc(atributoOrdenacao));
		crit.setFirstResult(comeco);
		crit.setMaxResults(quantidade);
		return crit.list();

	}

	@SuppressWarnings("unchecked")
	public List<T> getListaTodosPaginacao(String atributoOrdenacao, String dir, int comeco, int quantidade,
			List<Criterion> restricoes) throws Exception {
		Session session = (Session) manager.getDelegate();
		Criteria crit = session.createCriteria(getClasseEntidade());
		if (dir.equals("asc"))
			crit.addOrder(Order.asc(atributoOrdenacao));
		else
			crit.addOrder(Order.desc(atributoOrdenacao));

		restricoes.forEach(crit::add);

		crit.setFirstResult(comeco);
		crit.setMaxResults(quantidade);
		return crit.list();

	}

	public long getTotalRegistros() {
		Session session = (Session) manager.getDelegate();
		Criteria crit = session.createCriteria(getClasseEntidade());
		crit.setProjection(Projections.count("id"));

		return (Long) crit.uniqueResult();
	}

	public long getTotalRegistros(List<Criterion> restricoes) {
		Session session = (Session) manager.getDelegate();
		Criteria crit = session.createCriteria(getClasseEntidade());
		crit.setProjection(Projections.count("id"));

		restricoes.forEach(crit::add);

		return (Long) crit.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<T> getListaPorAtributos(HashMap<String, Object> atributos, String atributoOrdenacao) throws Exception {
		Class<? extends EntityBase> classe = getClasseEntidade();
		Session session = (Session) manager.getDelegate();
		Criteria crit = session.createCriteria(classe);
		for (Entry<String, Object> a : atributos.entrySet()) {
			crit.add(Restrictions.eq(a.getKey(), a.getValue()));
		}

		if (!ModelUtils.isNullEmpty(atributoOrdenacao))
			crit.addOrder(Order.asc(atributoOrdenacao));

		return crit.list();
	}

	@SuppressWarnings("unchecked")
	public T getObjetoPorAtributos(HashMap<String, Object> atributos) {
		Class<? extends EntityBase> classe = getClasseEntidade();
		Session session = (Session) manager.getDelegate();
		Criteria crit = session.createCriteria(classe);
		for (Entry<String, Object> a : atributos.entrySet()) {
			crit.add(Restrictions.eq(a.getKey(), a.getValue()));
		}

		crit.setMaxResults(1);

		return (T) crit.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<T> getListaPorAtributos(HashMap<String, Object> atributos, List<Order> ordem) throws Exception {
		Class<? extends EntityBase> classe = getClasseEntidade();
		Session session = (Session) manager.getDelegate();
		Criteria crit = session.createCriteria(classe);
		for (Entry<String, Object> a : atributos.entrySet()) {
			crit.add(Restrictions.eq(a.getKey(), a.getValue()));
		}

		ordem.forEach(crit::addOrder);

		return crit.list();
	}

	@SuppressWarnings("unchecked")
	public List<T> getListaPorAtributos(List<Criterion> restricoes) throws Exception {
		Class<? extends EntityBase> classe = getClasseEntidade();
		Session session = (Session) manager.getDelegate();
		Criteria crit = session.createCriteria(classe);
		restricoes.forEach(crit::add);

		return crit.list();
	}

	@SuppressWarnings("unchecked")
	public List<T> getListaPorAtributos(List<Criterion> restricoes, String atributoOrdenacao) throws Exception {
		Class<? extends EntityBase> classe = getClasseEntidade();
		Session session = (Session) manager.getDelegate();
		Criteria crit = session.createCriteria(classe);
		for (Criterion r : restricoes) {
			crit.add(r);
		}

		crit.addOrder(Order.asc(atributoOrdenacao));

		return crit.list();
	}

	@SuppressWarnings("unchecked")
	public List<T> getListaPorAtributos(List<Criterion> restricoes, List<String> joins) throws Exception {
		Class<? extends EntityBase> classe = getClasseEntidade();
		Session session = (Session) manager.getDelegate();
		Criteria crit = session.createCriteria(classe);
		for (String join : joins)
			crit.createAlias(join, join, JoinType.INNER_JOIN);

		Criterion[] restricoesArray = new Criterion[restricoes.size()];
		restricoesArray = restricoes.toArray(restricoesArray);

		crit.add(Restrictions.or(restricoesArray));

		return crit.list();
	}

	@SuppressWarnings("unchecked")
	public List<T> getListaPorAtributos(List<Criterion> restricoes, List<String> joins, String atributoOrdenacao)
			throws Exception {
		Class<? extends EntityBase> classe = getClasseEntidade();
		Session session = (Session) manager.getDelegate();
		Criteria crit = session.createCriteria(classe);
		for (String join : joins)
			crit.createAlias(join, join, JoinType.INNER_JOIN);

		Criterion[] restricoesArray = new Criterion[restricoes.size()];
		restricoesArray = restricoes.toArray(restricoesArray);

		crit.add(Restrictions.or(restricoesArray));

		crit.addOrder(Order.asc(atributoOrdenacao));

		return crit.list();
	}

	@SuppressWarnings("unchecked")
	public List<T> getListaTodos(HashMap<String, String> joins, List<Criterion> restricoes, String atributoOrdenacao)
			throws Exception {
		Class<? extends EntityBase> classe = getClasseEntidade();
		Session session = (Session) manager.getDelegate();
		Criteria crit = session.createCriteria(classe);
		for (Entry<String, String> j : joins.entrySet()) {
			crit.createAlias(j.getKey(), j.getValue(), JoinType.INNER_JOIN);
		}

		restricoes.forEach(crit::add);

		crit.addOrder(Order.asc(atributoOrdenacao));

		return crit.list();
	}

	@SuppressWarnings("unchecked")
	public List<T> getListaTodos(HashMap<String, String> joins) throws Exception {
		Class<? extends EntityBase> classe = getClasseEntidade();
		Session session = (Session) manager.getDelegate();
		Criteria crit = session.createCriteria(classe);
		for (Entry<String, String> j : joins.entrySet()) {
			crit.createAlias(j.getKey(), j.getValue(), JoinType.INNER_JOIN);
		}
		crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

		return crit.list();
	}

	// Metoto para conusltar todos os objetos ordenando por um atributo
	// especifico
	@SuppressWarnings("unchecked")
	public List<T> getListaTodos(String atributoOrdenacao) throws Exception {
		String classe = getClasseEntidade().getSimpleName();
		return manager.createQuery("SELECT x FROM " + classe + " x ORDER BY " + atributoOrdenacao).getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<T> getListaTodos(HashMap<String, String> joins, String atributoOrdenacao) throws Exception {
		Class<? extends EntityBase> classe = getClasseEntidade();
		Session session = (Session) manager.getDelegate();
		Criteria crit = session.createCriteria(classe);
		for (Entry<String, String> j : joins.entrySet()) {
			crit.createAlias(j.getKey(), j.getValue(), JoinType.INNER_JOIN);
		}

		crit.addOrder(Order.asc(atributoOrdenacao));

		return crit.list();
	}

	// Metoto para conusltar todos os objetos ordenando por um atributo
	// especifico
	@SuppressWarnings("unchecked")
	public List<T> getListaPorAtributo(String atributo, Object valor) throws Exception {
		String classe = getClasseEntidade().getSimpleName();

		return manager.createQuery("SELECT x FROM " + classe + " x WHERE x." + atributo + " = :valor")
				.setParameter("valor", valor).getResultList();
	}

	// Metoto para um objeto por atributo especifico
	@SuppressWarnings("unchecked")
	public T getObjetoPorAtributo(String atributo, Object valor) throws Exception {
		String classe = getClasseEntidade().getSimpleName();
		try {
			return (T) manager.createQuery("SELECT x FROM " + classe + " x WHERE x." + atributo + " = :valor")
					.setParameter("valor", valor).setMaxResults(1).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public T getObjeto(Object id) throws Exception {
		Class<? extends EntityBase> classe = getClasseEntidade();
		try {
			return (T) manager.find(classe, id);
		} catch (NoResultException e) {
			return null;
		}
	}

	// Metoto para retornar um objeto por atributo especifico com ordenacao
	@SuppressWarnings("unchecked")
	public T getObjetoPorAtributo(String atributo, String valor, String atributoOrdenacao) throws Exception {
		String classe = getClasseEntidade().getSimpleName();
		try {
			return (T) manager.createQuery(
					"SELECT x FROM " + classe + " x WHERE x." + atributo + " = :valor ORDER BY " + atributoOrdenacao)
					.setParameter("valor", valor).setMaxResults(1).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	// Metoto para conusltar se um objeto existe na tabela atraves de atributo
	// especifico
	@SuppressWarnings("unchecked")
	public boolean existeObjetoPorAtributo(String atributo, Object valor) throws Exception {
		try {
			Class<? extends EntityBase> classe = getClasseEntidade();

			Session session = (Session) manager.getDelegate();
			Criteria crit = session.createCriteria(classe);

			crit.add(Restrictions.eq(atributo, valor));

			Object o = crit.uniqueResult();

			return o != null;
		} catch (HibernateException e) {
			return true;
		}
	}

	// Metoto para conusltar se um objeto existe na tabela atraves de atributos
	// especificos
	@SuppressWarnings("unchecked")
	public boolean existeObjetoPorAtributos(HashMap<String, Object> atributos) throws Exception {
		try {
			Class<? extends EntityBase> classe = getClasseEntidade();

			Session session = (Session) manager.getDelegate();
			Criteria crit = session.createCriteria(classe);

			for (Entry<String, Object> a : atributos.entrySet()) {
				crit.add(Restrictions.eq(a.getKey(), a.getValue()));
			}

			Object o = crit.uniqueResult();

			return o == null ? false : true;
		} catch (HibernateException e) {
			return true;
		}
	}

	// Metoto para conusltar se um objeto existe na tabela atraves de atributos
	// especificos
	@SuppressWarnings("unchecked")
	public boolean existeObjetoPorAtributos(HashMap<String, Object> atributos, String ordenacao) throws Exception {
		try {
			Class<? extends EntityBase> classe = getClasseEntidade();

			Session session = (Session) manager.getDelegate();
			Criteria crit = session.createCriteria(classe);

			for (Entry<String, Object> a : atributos.entrySet()) {
				crit.add(Restrictions.eq(a.getKey(), a.getValue()));
			}

			crit.addOrder(Order.asc(ordenacao));

			Object o = crit.uniqueResult();

			return o == null ? false : true;
		} catch (HibernateException e) {
			return true;
		}
	}

	// Metoto para conusltar todos os objetos ordenando por um atributo
	// especifico
	@SuppressWarnings("unchecked")
	public List<T> getListaPorAtributo(String atributo, Object valor, String atributoOrdenacao) throws Exception {
		String classe = getClasseEntidade().getSimpleName();
		return manager.createQuery(
				"SELECT x FROM " + classe + " x WHERE x." + atributo + " = :valor ORDER BY " + atributoOrdenacao)
				.setParameter("valor", valor).getResultList();
	}

	@SuppressWarnings("unchecked")
	public T getPrimeiroObjeto(String atributo, Object valor, String atributoOrdenacao, boolean asc) throws Exception {
		if (!asc && atributoOrdenacao.contains(",")) {
			String res = "";
			String[] split = atributoOrdenacao.split(",");
			for (int i = 0; i < split.length; i++) {
				String s = split[i];
				if (i == split.length - 1)
					res += s;
				else
					res += s + " desc,";
			}
			atributoOrdenacao = res;
		}
		String classe = getClasseEntidade().getSimpleName();
		String order = asc ? "" : " desc";
		@SuppressWarnings("rawtypes")
		List lista = manager.createQuery("SELECT x FROM " + classe + " x WHERE x." + atributo + " = :valor ORDER BY "
				+ atributoOrdenacao + order).setParameter("valor", valor).setMaxResults(1).getResultList();
		if (ModelUtils.isListaValida(lista))
			return (T) lista.get(0);

		return null;
	}

	protected Criteria getCriteria(Class<?> classe) {
		Session session = (Session) manager.getDelegate();
		return session.createCriteria(classe);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Class getClasseEntidade() {
		if (this.modelClass == null) {
			if (getClass().getGenericSuperclass() instanceof ParameterizedType) {
				this.modelClass = (Class) ((ParameterizedType) getClass().getGenericSuperclass())
						.getActualTypeArguments()[0];
			} else if (((Class) getClass().getGenericSuperclass())
					.getGenericSuperclass() instanceof ParameterizedType) {
				this.modelClass = (Class<T>) ((ParameterizedType) ((Class) getClass().getGenericSuperclass())
						.getGenericSuperclass()).getActualTypeArguments()[0];
			}
		}
		return this.modelClass;
	}

}
