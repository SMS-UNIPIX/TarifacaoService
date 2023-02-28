package br.com.unipix.api.extream.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Table(name = "ConsumoDiarioSMS")
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConsumoDiarioSMS {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ConsumoDiarioSMSID")	
	private Long id;
	
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
