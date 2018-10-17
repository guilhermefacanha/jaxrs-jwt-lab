package core.business;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;

import core.dao.DAOBase;
import core.entity.EntityBase;

public abstract class BusinessBase<T extends EntityBase> implements Serializable
{
	private static final long serialVersionUID = 7392952886823572130L;

	public abstract DAOBase<T> getDao();

	public void salvar(T entidade) throws Exception
	{
		getDao().salvar(entidade);
	}

	public void remover(T entidade) throws Exception
	{
		getDao().remover(entidade);
	}

	public void remover(long id) throws Exception
	{
		getDao().remover(id);
	}
	
	public T getPrimeiroObjeto(String atributo, Object valor, String atributoOrdenacao, boolean asc) throws Exception
	{
		return getDao().getPrimeiroObjeto(atributo, valor, atributoOrdenacao, asc);
	}
	
	public List<T> getListaTodosPaginacao(String atributoOrdenacao, String dir, int comeco, int quantidade) throws Exception
	{
		return getDao().getListaTodosPaginacao(atributoOrdenacao,dir,comeco,quantidade);
	}
	
	public List<T> getListaTodosPaginacao(String atributoOrdenacao, String dir, int comeco, int quantidade, List<Criterion> restricoes) throws Exception
	{
		return getDao().getListaTodosPaginacao(atributoOrdenacao,dir,comeco,quantidade,restricoes);
	}
	
	public long getTotalRegistros(List<Criterion> restricoes)
	{
		return getDao().getTotalRegistros(restricoes);
	}
	
	public long getTotalRegistros()
	{
		return getDao().getTotalRegistros();
	}
	
	public List<T> getListaTodos() throws Exception
	{
		return getDao().getListaTodos();
	}
	
	public List<T> getListaTodos(HashMap<String, String> joins, List<Criterion> restricoes, String atributoOrdenacao) throws Exception
	{
		return getDao().getListaTodos(joins, restricoes, atributoOrdenacao);
	}

	public List<T> getListaTodos(HashMap<String, String> joins) throws Exception
	{
		return getDao().getListaTodos(joins);
	}

	public List<T> getListaTodos(String atributoOrdenacao) throws Exception
	{
		return getDao().getListaTodos(atributoOrdenacao);
	}

	public List<T> getListaTodos(HashMap<String, String> joins, String atributoOrdenacao) throws Exception
	{
		return getDao().getListaTodos(joins, atributoOrdenacao);
	}

	public List<T> getListaPorAtributo(String atributo, Object valor) throws Exception
	{
		return getDao().getListaPorAtributo(atributo, valor);
	}

	public T getObjetoPorAtributo(String atributo, Object valor) throws Exception
	{
		return getDao().getObjetoPorAtributo(atributo, valor);
	}
	
	public T getObjetoPorAtributos(HashMap<String, Object> atributos) throws Exception
	{
		return getDao().getObjetoPorAtributos(atributos);
	}

	public List<T> getListaPorAtributos(HashMap<String, Object> atributos, String atributoOrdenacao) throws Exception
	{
		return getDao().getListaPorAtributos(atributos, atributoOrdenacao);
	}

	public List<T> getListaPorAtributos(HashMap<String, Object> atributos, List<Order> ordens) throws Exception
	{
		return getDao().getListaPorAtributos(atributos, ordens);
	}

	public List<T> getListaPorAtributos(List<Criterion> restricoes) throws Exception
	{
		return getDao().getListaPorAtributos(restricoes);
	}

	public List<T> getListaPorAtributos(List<Criterion> restricoes, List<String> joins) throws Exception
	{
		return getDao().getListaPorAtributos(restricoes,joins);
	}
	
	public List<T> getListaPorAtributos(List<Criterion> restricoes, List<String> joins, String atributoOrdenacao) throws Exception
	{
		return getDao().getListaPorAtributos(restricoes,joins,atributoOrdenacao);
	}

	public List<T> getListaPorAtributos(List<Criterion> restricoes, String atributoOrdenacao) throws Exception
	{
		return getDao().getListaPorAtributos(restricoes, atributoOrdenacao);
	}

	public T getObjeto(Object id) throws Exception
	{
		return getDao().getObjeto(id);
	}

	public T getObjetoPorAtributo(String atributo, String valor, String atributoOrdenacao) throws Exception
	{
		return getDao().getObjetoPorAtributo(atributo, valor, atributoOrdenacao);
	}

	public boolean existeObjetoPorAtributo(String atributo, String valor) throws Exception
	{
		return getDao().existeObjetoPorAtributo(atributo, valor);
	}

	public boolean existeObjetoPorAtributos(HashMap<String, Object> atributos) throws Exception
	{
		return getDao().existeObjetoPorAtributos(atributos);
	}

	public boolean existeObjetoPorAtributos(HashMap<String, Object> atributos, String atributoOrdenacao) throws Exception
	{
		return getDao().existeObjetoPorAtributos(atributos,atributoOrdenacao);
	}

	public List<T> getListaPorAtributo(String atributo, Object valor, String atributoOrdenacao) throws Exception
	{
		return getDao().getListaPorAtributo(atributo, valor, atributoOrdenacao);
	}

}
