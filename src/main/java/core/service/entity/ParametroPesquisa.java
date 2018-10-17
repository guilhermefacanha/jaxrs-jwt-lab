package core.service.entity;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ParametroPesquisa {
	String ordenacao;
	
	List<ParValor> listaPares;
	List<String> joins;
}
