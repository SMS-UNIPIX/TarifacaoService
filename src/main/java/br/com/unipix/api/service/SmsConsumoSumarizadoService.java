package br.com.unipix.api.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.math3.util.Precision;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.unipix.api.dto.ClienteProdutoDto;
import br.com.unipix.api.extream.model.ConsumoDiarioSMS;
import br.com.unipix.api.extream.repository.ConsumoDiarioSMSRepository;
import br.com.unipix.api.model.Cliente;
import br.com.unipix.api.model.ClienteProduto;
import br.com.unipix.api.model.Produto;
import br.com.unipix.api.mongo.model.CampanhaDashboard;
import br.com.unipix.api.mongo.model.CampanhaDocument;
import br.com.unipix.api.mongo.model.RespostaMensagem;
import br.com.unipix.api.mongo.repository.CampanhaDashboardRepository;
import br.com.unipix.api.mongo.repository.CampanhaDocumentRepository;
import br.com.unipix.api.mongo.repository.RespostaRepository;
import br.com.unipix.api.repository.ClienteProdutoRepository;
import br.com.unipix.api.repository.ClienteRepository;
import br.com.unipix.api.repository.ProdutoRepository;

@Service
@Component 
@EnableScheduling
public class SmsConsumoSumarizadoService {

	Logger log = LoggerFactory.getLogger(this.getClass().getName());

	@Autowired
	private ClienteProdutoRepository clienteProdutoRepository;

	@Autowired
	private ConsumoDiarioSMSRepository consumoDiarioRepository;

	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private ProdutoRepository produtoRepository;

	@Autowired
	private RespostaRepository respostaRepository;
	
	@Autowired
	private CampanhaDocumentRepository campanhaRepository;

	@Autowired
	private CampanhaDashboardRepository campanhaDashRepository;

    @Autowired
    private MongoTemplate mongoTemplate;
    
    //@Autowired
    //private JdbcTemplate jdbcTemplate;
    
	@Scheduled(cron = "0 0 2 * * *")
	//@Scheduled(fixedRate = 100000000)
	//@Scheduled(cron = "0 0 2 * * *", zone = "America/Sao_Paulo")
	public void executar() {
		//String dateStr = "2023-01-16T00:00:00";
	    //LocalDateTime dataFinal = LocalDateTime.parse(dateStr).minusDays(1);
		LocalDateTime dataFinal = LocalDateTime.now().minusDays(1);
		LocalDateTime dataInicial = dataFinal.withDayOfMonth(1);
		while (true){
			if(dataInicial.isBefore(dataFinal) || dataInicial.isEqual(dataFinal)){
				log.info(dataInicial.toString());
				processaDia(dataInicial);
				dataInicial = dataInicial.plusDays(1);
			}else {
				log.info("*****Fim");
				break;
			}
		}
		//refasDocRespostaSms();
		//log.info("*****Fim");
	}

	public void processaDia(LocalDateTime data) {
		List<ConsumoDiarioSMS> sumarizados = new ArrayList<ConsumoDiarioSMS>();

		LocalDateTime dataInicial = data.withHour(0).withMinute(0).withSecond(0);
		LocalDateTime dataFinal = data.withHour(23).withMinute(59).withSecond(59);

        Date dataInicialFormatada = Date.from(dataInicial.withSecond(0).withNano(0).atZone(ZoneId.systemDefault()).toInstant());
        Date dataFinalFormatada = Date.from(dataFinal.withSecond(59).withNano(999999999).atZone(ZoneId.systemDefault()).toInstant());
        
        List<Cliente> clientes = clienteRepository.findByAllCliente();
        
        for (Cliente cli : clientes) {
        	//Verificar os envios
            List<CampanhaDocument> listaDia = campanhaRepository.obterVendaProdutoClientePeriodoArquivo(dataInicialFormatada, dataFinalFormatada, cli.getId());
            if(listaDia.size()>0) {
            	for (CampanhaDocument c : listaDia) {
            		ClienteProdutoDto dto = clienteProduto(c.getClienteId(), c.getProdutoId());
            		ConsumoDiarioSMS consumo = ConsumoDiarioSMS.builder()
            				.clienteId(c.getClienteId())
            				.clienteNome(c.getClienteNome())
            				.produtoId(c.getProdutoId())
            				.produtoNome(c.getProdutoNome())
            				.dataCadastroConta(dto.getDataCadastroProduto())
            				.faturamentoId(dto.getFaturamentoId())
            				.modalidade(cli.getFormaPagamento().getNome())
            				.tarifa(dto.getTarifa())
            				.qtdeEnvio(c.getQuantidadeEnvios())
            				.qtdeResposta(0L)
            				.valorEnvio(0D)
            				.valorResposta(0D)
            				.dia(dataInicial)
            				.diaSemana(diaSemana(dataInicial.getDayOfWeek().name()))
            				.build();
            		
            		consumo.setValorEnvio(Precision.round((consumo.getTarifa()!=null?consumo.getTarifa()*consumo.getQtdeEnvio(): 0L), 4));
         		
            		sumarizados.add(consumo);
            	}
            }
            
            //Verificar as Respostas
           	List<RespostaMensagem> respostas = respostaRepository.countRespostaMensagemByCliente(cli.getId(), dataInicialFormatada, dataFinalFormatada);            
           	if(respostas.size()>0){
           		for (RespostaMensagem qtdeResp : respostas) {
           			Long idProduto = 0L;
           			String produto = qtdeResp.getId().substring(qtdeResp.getId().indexOf("\"idProduto\": ")+13, qtdeResp.getId().indexOf("}"));
           			try {
           				idProduto = Long.parseLong(produto);
					} catch (NumberFormatException e) {
						log.info("Produto: "+produto);
					}
           			List<ClienteProdutoDto> dtos = clienteProdutoLista(cli.getId());
           			for (ClienteProdutoDto dto : dtos) {
           				ConsumoDiarioSMS consumo = ConsumoDiarioSMS.builder()
           					.clienteId(cli.getId())
           					.clienteNome(cli.getNome())
           					.produtoId(dto.getProdutoId())
           					.produtoNome(dto.getProdutoNome())
           					.dataCadastroConta(dto.getDataCadastroProduto())
           					.faturamentoId(dto.getFaturamentoId())
           					.modalidade(cli.getFormaPagamento().getNome())
           					.tarifa(dto.getTarifa())
           					.qtdeEnvio(0L)
           					.qtdeResposta((idProduto==dto.getProdutoId()?qtdeResp.getQuantidade().longValue():0L))
           					.valorEnvio(0D)
           					.valorResposta(0D)
           					.valorEnvio(0D)
           					.valorResposta(0D)
           					.dia(dataInicial)
           					.diaSemana(diaSemana(dataInicial.getDayOfWeek().name()))
           					.build();
               		
           				consumo.setValorResposta(Precision.round((consumo.getTarifa()!=null?consumo.getTarifa()*(consumo.getQtdeResposta()!=null?consumo.getQtdeResposta():0L):0L), 4));
               		
           				sumarizados.add(consumo);	
           			}
           		}
			}
           	
           	//Caso não tenha envios/respostas
            if(listaDia.size()==0 && respostas.size()==0){
            	List<ClienteProdutoDto> dtos = clienteProdutoLista(cli.getId());
            	for (ClienteProdutoDto dto : dtos) {
            		ConsumoDiarioSMS consumo = ConsumoDiarioSMS.builder()
        				.clienteId(cli.getId())
        				.clienteNome(cli.getNome())
        				.produtoId(dto.getProdutoId())
        				.produtoNome(dto.getProdutoNome())
        				.dataCadastroConta(dto.getDataCadastroProduto())
        				.faturamentoId(dto.getFaturamentoId())
        				.modalidade(cli.getFormaPagamento().getNome())
        				.tarifa(dto.getTarifa())
        				.qtdeEnvio(0L)
        				.qtdeResposta(0L)
        				.valorEnvio(0D)
        				.valorResposta(0D)
        				.valorEnvio(0D)
        				.valorResposta(0D)
        				.dia(dataInicial)
        				.diaSemana(diaSemana(dataInicial.getDayOfWeek().name()))
        				.build();
            		
            		sumarizados.add(consumo);	
				}
            }
		}
        
        if(sumarizados.size()>0) {
        	consumoDiarioRepository.deletarConsumoSMS(dataInicial, dataFinal);
        	consumoDiarioRepository.saveAll(sumarizados);
        	
        	//campanhaRepository.removerRegistrosDia(dataInicialFormatada, dataFinalFormatada);
        	//campanhaRepository.salvar(sumarizados);
        }
	}

	public String formaPagamento(Long clienteId) {
		String modalidade = "";
		Optional<Cliente> cliente = clienteRepository.findById(clienteId);
		if(cliente != null) {
			modalidade = cliente.get().getFormaPagamento().getNome();
		}
		return modalidade;
	}

	public ClienteProdutoDto clienteProduto(Long clienteId, Long produtoId) {
		ClienteProdutoDto dto = new ClienteProdutoDto();
		ClienteProduto cliProd = clienteProdutoRepository.findByAllClienteProduto(clienteId, produtoId);
		if(cliProd != null) {
			dto.setFaturamentoId(cliProd.getFaturamentoId());
			dto.setTarifa(cliProd.getValor().doubleValue());
			dto.setDataCadastroProduto(cliProd.getDataCadastro());
		}
		return dto;
	}
	
	public List<ClienteProdutoDto> clienteProdutoLista(Long clienteId) {
		List<ClienteProdutoDto> cliProResponse = new ArrayList<ClienteProdutoDto>();
		
		List<ClienteProduto> cliPros = clienteProdutoRepository.findByAllClienteProdutoList(clienteId);
		if(cliPros.size()>0) {
			for (ClienteProduto cp : cliPros) {
				ClienteProdutoDto clienteProduto = ClienteProdutoDto.builder()
					.produtoId(cp.getProdutoId())
					.produtoNome(produto(cp.getProdutoId()))
					.faturamentoId(cp.getFaturamentoId())
					.tarifa(cp.getValor().doubleValue())
					.dataCadastroProduto(cp.getDataCadastro()).build();
				
				cliProResponse.add(clienteProduto);
			}

		}
		return cliProResponse;
	}
	
	public String produto(Long produtoId) {
		String produtoNome = "";
		Optional<Produto> produto = produtoRepository.findById(produtoId);
		if(produto != null) {
			produtoNome = produto.get().getNome();
		}
		return produtoNome;
	}

	public String diaSemana(String diaSemana) {
		String nome = "";
		
		switch (diaSemana) {
        case "SUNDAY":
            nome = "domingo";
            break;
        case "MONDAY":
            nome = "segunda";
            break;
        case "TUESDAY":
        	nome = "terca";
        	break;
        case "WEDNESDAY":
        	nome = "quarta";
            break;
        case "THURSDAY":
        	nome = "quinta";
            break;
        case "FRIDAY":
        	nome = "sexta";
            break;
        case "SATURDAY":
        	nome = "sabado";
		}
		return nome;
	}

	public void refasDocRespostaSms() {
		LocalDateTime dataI =  LocalDateTime.parse("2022-11-01T00:00:00");
		LocalDateTime dataF =  LocalDateTime.parse("2022-11-30T00:00:00");

		LocalDateTime dataInicial = dataI.withHour(0).withMinute(0).withSecond(0);
		LocalDateTime dataFinal = dataF.withHour(23).withMinute(59).withSecond(59);

        Date dataInicialFormatada = Date.from(dataInicial.withSecond(0).withNano(0).atZone(ZoneId.systemDefault()).toInstant());
        Date dataFinalFormatada = Date.from(dataFinal.withSecond(59).withNano(999999999).atZone(ZoneId.systemDefault()).toInstant());
		
		List<RespostaMensagem> resposta = respostaRepository.respostaMensagemByProduto(dataInicialFormatada, dataFinalFormatada);
		
		for (RespostaMensagem resp : resposta) {
			CampanhaDashboard dash = campanhaDashRepository.findByCampaingId(resp.getIdCampanhaSql());
			if(dash != null) {
				RespostaMensagem respostaSms = RespostaMensagem.builder()
					.custo(resp.getCusto())
					.data(resp.getData())
					.dataEnvio(resp.getDataEnvio())
					.dataStatus(resp.getDataStatus())
					.idCampanhaSql(resp.getIdCampanhaSql())
					.idCentroCusto(resp.getIdCentroCusto())
					.idCliente(resp.getIdCliente())
					.idFornecedor(resp.getIdFornecedor())
					.idProduto(dash.getProduto().getId())
					.listaBloqueio(resp.getListaBloqueio())
					.listaContato(resp.getListaContato())
					.mensagemEnvio(resp.getMensagemEnvio())
					.nomeCampanha(resp.getNomeCampanha())
					.nomeCentroCusto(resp.getNomeCentroCusto())
					.nomeCliente(resp.getNomeCliente())
					.nomeFornecedor(resp.getNomeFornecedor())
					.nomeProduto(dash.getProduto().getNome())
					.numero(resp.getNumero())
					.operadora(resp.getOperadora())
					.quantidade(resp.getQuantidade())
					.resposta(resp.getResposta())
					.respostaAutomatica(resp.getRespostaAutomatica())
					.respostaUpperCase(resp.getRespostaUpperCase())
					.smsId(resp.getSmsId())
					.smsClienteId(resp.getSmsClienteId())					
					.tarifa(resp.getTarifa())
					.build();
				
				deletar(resp.getId());
				mongoTemplate.insert(respostaSms, "respostaSms");
			}
		}
	}

    @Transactional
    public void deletar(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        mongoTemplate.remove(query, RespostaMensagem.class, "respostaSms");
    }

	
}