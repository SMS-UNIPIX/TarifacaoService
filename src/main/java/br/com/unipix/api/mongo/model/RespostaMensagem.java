package br.com.unipix.api.mongo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document("respostaSms")
public class RespostaMensagem {
	@Id
	private String id;
	private Long idCliente;
	private String nomeCliente;
	private Long idCentroCusto;
	private Long idCampanhaSql;
	private Long idFornecedor;
	private String nomeFornecedor;
	private Double tarifa;
	private Double custo;
	private String numero;
	private String resposta;
	private String operadora;
	private String nomeCampanha;
	private String mensagemEnvio;	
	private String nomeCentroCusto;
	private String respostaUpperCase;
	private BigDecimal quantidade;
	private LocalDateTime data;
	private LocalDateTime dataEnvio;	
	private LocalDateTime dataStatus;	
	private String smsId;
	private Long idProduto;
	private String nomeProduto;
	private String respostaAutomatica;
	@Builder.Default
	private List<String> listaBloqueio = new ArrayList<>();
	@Builder.Default
	private List<String> listaContato = new ArrayList<>();
	private String smsClienteId;
	private String tipoResposta;
	private String api;

}
