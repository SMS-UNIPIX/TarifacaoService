package br.com.unipix.api.mongo.model;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "smsConsumoSumarizado")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SmsConsumoSumarizado {
	
	private Long clienteId;
	private String clienteNome;	
	private Long produtoId;
	private String produtoNome;	
	private LocalDateTime dataCadastroConta;	
	private Long faturamentoId;
	private String modalidade;	
	private Double tarifa;	
	private Long qtdeEnvio;
	private Long qtdeResposta;	
	private Double valorEnvio;
	private Double valorResposta;
	private LocalDateTime dia;
	private String diaSemana;	
}
