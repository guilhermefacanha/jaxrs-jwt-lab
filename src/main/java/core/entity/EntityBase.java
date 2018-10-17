package core.entity;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonIgnore;

import core.util.ModelUtils;
import lombok.Data;

@Data
@MappedSuperclass
public abstract class EntityBase implements Serializable
{
	private static final long serialVersionUID = 5641638789450652037L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected long id;

	@JsonIgnore
	@Version
	private long version;
	
	@JsonIgnore
	@Transient
	private String idBase64;
	
	@JsonIgnore
	public String getIdBase()
	{
		return ModelUtils.gerarBase64(String.valueOf(id));
	}
	
	public void verificarIdBase(){
		try {
			this.id = Long.parseLong(ModelUtils.lerBase64(idBase64));
		} catch (Exception e) {
		}
	}

}
