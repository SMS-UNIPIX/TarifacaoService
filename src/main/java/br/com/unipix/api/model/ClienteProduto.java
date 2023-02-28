package br.com.unipix.api.model;

import java.math.BigDecimal;
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
@Entity
@Table(name = "tb_cliente_produto")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClienteProduto {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "cliente_id")
	private Long clienteId;

	@Column(name = "produto_id")	
	private Long produtoId;
	
	@Column(name = "valor")
	private BigDecimal valor;
	
	private Boolean ativo;
	
	@Column(name = "faturamento_id")
	private Long faturamentoId;
	
	@Column(name = "data_cadastro")
	private LocalDateTime dataCadastro;
	
}
