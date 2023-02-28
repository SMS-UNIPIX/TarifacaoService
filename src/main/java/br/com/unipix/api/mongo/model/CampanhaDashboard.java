package br.com.unipix.api.mongo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "campanhaDashboard")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CampanhaDashboard {
	
	@Id
	private String id;
	private String nomeCampanha;
	private String mensagemCampanha;
	@Builder.Default
	private Double totalCusto = 0D;
	@Builder.Default
	private Double totalTarifa = 0D;
	private Long idCampanhaSql;
	private Long higienizacaoId;
	private Long cancelado;
	private Long blackList;
	private Long invalidos;
	private Long repetidos;
	private Long validos;
	private Long processados;
	private Long enviosUnitarios;
	private Long clienteId;
	private Long enviados;
	private Long respostas;
	private Long respostasAutomaticas;
	private ProdutoMongo produto;

}