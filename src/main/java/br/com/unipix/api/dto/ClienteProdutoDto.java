package br.com.unipix.api.dto;

import java.time.LocalDateTime;

import br.com.unipix.api.enumaration.FormaPagamentoEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;@Data


@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClienteProdutoDto {

	private Long clienteId;
	private String clienteNome;
	private Long produtoId;
	private String produtoNome;
	private FormaPagamentoEnum modalidade;
	private Double tarifa;	
	private Long faturamentoId;
	private LocalDateTime dataCadastroProduto;
}
