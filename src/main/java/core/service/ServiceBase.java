package core.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import core.business.BusinessBase;
import core.entity.EntityBase;
import core.service.entity.ParValor;
import core.service.entity.ParametroPesquisa;
import core.service.entity.RetornoNegocioException;
import core.util.ModelUtils;
import rest.manager.annotations.JWTTokenNeeded;

public abstract class ServiceBase<T extends EntityBase> implements Serializable {

	private static final long serialVersionUID = -2109810374032274822L;

	public abstract BusinessBase<T> getBusiness();

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/contains")
	public Response existeObjetoPorAtributos(ParametroPesquisa pesquisa) {
		try {
			HashMap<String, Object> atributos = getAtributos(pesquisa);

			boolean existe;

			existe = getBusiness().existeObjetoPorAtributos(atributos);

			return Response.ok(existe).build();
		} catch (Exception e) {
			return Response.ok(RetornoNegocioException.builder().erro("Erro ao realizar consulta existeObjetoPorAtributos").exception(e.getMessage()).build()).status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/list")
	public Response getListaPorAtributos(ParametroPesquisa pesquisa) {
		try {
			String ordenacao = getOrdenacao(pesquisa);
			HashMap<String, Object> atributos = getAtributos(pesquisa);

			List<T> lista;

			lista = getBusiness().getListaPorAtributos(atributos, ordenacao);

			return Response.ok(lista).build();
		} catch (Exception e) {
			return Response.ok(RetornoNegocioException.builder().erro("Erro ao realizar consulta getListaPorAtributos").exception(e.getMessage()).build()).status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	@JWTTokenNeeded
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/get")
	public Response getObjetoPorAtributos(ParametroPesquisa pesquisa, @HeaderParam("Authorization") String auth) {
		try {
			HashMap<String, Object> atributos = getAtributos(pesquisa);
			Long id = contemId(atributos);

			T entidade;

			if (id != null)
				entidade = getBusiness().getObjeto(id);
			else
				entidade = getBusiness().getObjetoPorAtributos(atributos);
			
			return Response.ok(entidade).build();
		} catch (Exception e) {
			return Response.ok(RetornoNegocioException.builder().erro("Erro ao realizar consulta getListaPorAtributos").exception(e.getMessage()).build()).status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	private Long contemId(HashMap<String, Object> atributos) {

		try {
			if (atributos.containsKey("id"))
				return Long.parseLong(atributos.get("id").toString());
		} catch (Exception e) {
			return null;
		}

		return null;
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/all")
	public Response getListaTodos(ParametroPesquisa pesquisa) {
		try {
			String ordenacao = getOrdenacao(pesquisa);

			List<T> lista;

			if (ModelUtils.isNullEmpty(ordenacao))
				lista = getBusiness().getListaTodos();
			else
				lista = getBusiness().getListaTodos(ordenacao);

			return Response.ok(lista).build();
		} catch (Exception e) {
			return Response.ok(RetornoNegocioException.builder().erro("Erro ao realizar consulta getListaTodos").exception(e.getMessage()).build()).status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/save")
	public Response getListaTodos(T entidade) {
		try {
			getBusiness().salvar(entidade);
			return Response.ok(entidade).build();
		} catch (Exception e) {
			return Response.ok(RetornoNegocioException.builder().erro("Erro ao salvar registro").exception(e.getMessage()).build()).status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/delete")
	public Response delete(T entidade) {
		try {
			long id = entidade.getId();
			getBusiness().remover(entidade.getId());
			return Response.ok("Registro " + id + " removido com sucesso").build();
		} catch (Exception e) {
			return Response.ok(RetornoNegocioException.builder().erro("Erro ao remover registro").exception(e.getMessage()).build()).status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	private HashMap<String, Object> getAtributos(ParametroPesquisa pesquisa) {
		HashMap<String, Object> mapa = new HashMap<String, Object>();
		if (pesquisa != null && pesquisa.getListaPares() != null) {
			for (ParValor par : pesquisa.getListaPares()) {
				mapa.put(par.getChave(), par.getValor());
			}
		}
		return mapa;
	}

	private String getOrdenacao(ParametroPesquisa pesquisa) {
		if (pesquisa != null && !ModelUtils.isNullEmpty(pesquisa.getOrdenacao()))
			return pesquisa.getOrdenacao();

		return null;
	}

}
