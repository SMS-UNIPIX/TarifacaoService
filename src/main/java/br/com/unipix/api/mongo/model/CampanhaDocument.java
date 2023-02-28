package br.com.unipix.api.mongo.model;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "campanha")
public class CampanhaDocument {

	@Id
	private String id;
	private Long idCampanhaSql;
	private String nomeCampanha;
	private String numero;
	private String mensagem;
	private LocalDateTime dataEnvio;
    private LocalDateTime dataAgendada;
    private LocalDateTime dataRetorno;
	private Long produtoId;
	private String produtoNome;
	private Long centroCustoId;
	private String centroCustoNome;
	private Long clienteId;
	private String clienteNome;
	private Long usuarioId;
	private String usuarioNome;
    private String operadora;
	private Long rotaId;
	private String rotaNome;
    private Long fornecedorId;
    private String fornecedorNome;
    private String mensagemFornecedor;
    private Long quantidadeCaracteres;
    @Builder.Default
    private Long quantidadeEnvios = 1L;
    private LocalDateTime dataStatus;
    private String status;
    @Builder.Default
    private Double tarifa = 0.00D;
    @Builder.Default
    private Double custo = 0.00D;
    private String situacao;
    private String agendado;
    private String pausado;
	private String smsClienteId;
	private String modalidade;
	
}
