package br.com.unipix.api.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.unipix.api.enumaration.StatusHistoricoEnum;
import br.com.unipix.api.model.ClienteProduto;
import br.com.unipix.api.model.HistoricoPrecoProduto;
import br.com.unipix.api.repository.ClienteProdutoRepository;
import br.com.unipix.api.repository.HistoricoPrecoProdutoRepository;

@Service
@Component 
@EnableScheduling
public class TarifacaoService {

	Logger log = LoggerFactory.getLogger(this.getClass().getName());

	@Autowired
	private	HistoricoPrecoProdutoRepository historicoPrecoRepository;
	
	@Autowired
	private ClienteProdutoRepository clienteProdutoRepository;

	@Scheduled(cron = "0 */1 * * * *")
	//@Scheduled(fixedDelay = 600000000)
	public void executar() {
		LocalDateTime dataAgora = LocalDateTime.now();
		log.info("*****Inicio : "+LocalDateTime.now());
		processaDia(dataAgora);
		log.info("*****Fim    : "+LocalDateTime.now());		
	}

	@Transactional
	public void processaDia(LocalDateTime dataAgora) {
		List<HistoricoPrecoProduto> listaDia = historicoPrecoRepository.findByAllDataAgendadaDia(dataAgora);
		
		if(listaDia!=null && listaDia.size()>0) {
			for (HistoricoPrecoProduto lista : listaDia) {
				ClienteProduto cliProd = clienteProdutoRepository.findByAllClienteProduto(lista.getClienteId().getId(), lista.getProdutoId().getId());
				if(cliProd!=null) {
					HistoricoPrecoProduto historicoAntigo = new HistoricoPrecoProduto();
					BigDecimal valorAnt = cliProd.getValor();

					//Atualizo o status de vigente para vencido
					List<HistoricoPrecoProduto> listaVigente = historicoPrecoRepository.findByAllClienteProdutoStatus(lista.getClienteId().getId(), lista.getProdutoId().getId(), StatusHistoricoEnum.VIGENTE);
					if(listaVigente!=null && listaVigente.size()>0) {
						historicoAntigo = HistoricoPrecoProduto.builder()
							.id(listaVigente.get(0).getId())
							.clienteId(listaVigente.get(0).getClienteId())
							.produtoId(listaVigente.get(0).getProdutoId())
							.usuarioId(listaVigente.get(0).getUsuarioId())
							.valor(listaVigente.get(0).getValor())
							.status(StatusHistoricoEnum.VENCIDO)
							.dataInicio(listaVigente.get(0).getDataInicio())
							.dataFim(dataAgora)
							.build();
					}else {//Caso não tenha nenhum status vigente
						historicoAntigo = HistoricoPrecoProduto.builder()
							.id(null)
							.clienteId(lista.getClienteId())
							.produtoId(lista.getProdutoId())
							.usuarioId(lista.getUsuarioId())
							.valor(valorAnt)
							.status(StatusHistoricoEnum.VENCIDO)
							.dataInicio(lista.getDataInicio())
							.dataFim(dataAgora)
							.build();						
					}
					historicoPrecoRepository.saveAndFlush(historicoAntigo);

					//Atualizo o status de agendado para vigente
					HistoricoPrecoProduto historicoNovo = HistoricoPrecoProduto.builder()
						.id(lista.getId())
						.clienteId(lista.getClienteId())
						.produtoId(lista.getProdutoId())
						.usuarioId(lista.getUsuarioId())
						.valor(lista.getValor())
						.status(StatusHistoricoEnum.VIGENTE)
						.dataInicio(dataAgora)
						.build();
					historicoPrecoRepository.saveAndFlush(historicoNovo);
					
					//Atualizo o valor do produto
					cliProd.setValor(lista.getValor());
					clienteProdutoRepository.saveAndFlush(cliProd);
					
					log.info("*****Cliente: "+historicoNovo.getClienteId().getId());
					log.info("*****Produto: "+historicoNovo.getProdutoId().getId());
					log.info("*****Dt.Inic: "+historicoNovo.getDataInicio());
					log.info("*****Valor  : "+historicoNovo.getValor());
				}
			}
		}
		
	}
	
}