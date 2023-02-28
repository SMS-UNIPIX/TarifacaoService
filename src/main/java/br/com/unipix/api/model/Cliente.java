package br.com.unipix.api.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import br.com.unipix.api.enumaration.FormaPagamentoEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Table(name = "tb_cliente")
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Cliente {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "nome")
	private String nome;
	
	@Column(name = "endereco")
	private String endereco;
	
	@Column(name = "bairro")
	private String bairro;
	
	@Column(name = "cidade")
	private String cidade;
	
	@Column(name = "uf")
	private String uf;
	
	@Column(name = "cep")
	private String cep;
	
	@Column(name = "cnpj")
	private String cnpj;

	@Column(name = "forma_pagamento")
	private FormaPagamentoEnum formaPagamento;

	@Column(name = "saldo_pre_pago")
	private BigDecimal saldoPrePago;
	
	private String urlCallbackResposta;
	private String urlCallbackEntrega;
	
	private Boolean ativo;

}
