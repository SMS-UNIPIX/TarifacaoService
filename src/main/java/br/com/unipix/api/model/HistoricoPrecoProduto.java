package br.com.unipix.api.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.unipix.api.enumaration.StatusHistoricoEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "tb_historico_preco_produto")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HistoricoPrecoProduto {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "cliente_id" , referencedColumnName = "id")
	@JsonBackReference
	@JsonIgnore
	private Cliente clienteId;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "produto_id" , referencedColumnName = "id")
	@JsonBackReference
	@JsonIgnore
	private Produto produtoId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "usuario_id" , referencedColumnName = "id")
	@JsonBackReference
	@JsonIgnore
	private Usuario usuarioId;
	
	@Column(name = "valor")
	private BigDecimal valor;
	
	@Column(name = "status")
	private StatusHistoricoEnum status;
		
	@Column(name = "data_inicio")
	private LocalDateTime dataInicio;
	
	@Column(name = "data_fim")
	private LocalDateTime dataFim;
	
}
