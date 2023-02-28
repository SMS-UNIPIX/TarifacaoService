package br.com.unipix.api.repository.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import br.com.unipix.api.dto.ClienteProdutoDto;
import br.com.unipix.api.repository.ClienteProdutoRepository;

@Repository
public class ClienteProdutoRepositoryImpl /*implements ClienteProdutoRepository*/{

	@PersistenceContext
	private EntityManager manager;
	
	public List<ClienteProdutoDto> findByAllCliente() {
		StringBuilder jpql = new StringBuilder();
		
		jpql.append("select "
				+ "		c.id as clienteId, \r\n"
				+ "		c.nome as clienteNome, \r\n"
				+ "		p.id as produtoId, \r\n"
				+ "		p.nome as produtoNome, \r\n"
				+ "     c.forma_pagamento as modalidade, \r\n"
				+ "     cp.valor as tarifa, \r\n"
				+ "     cp.faturamento_id as faturamentoId, \r\n"
				+ "     cp.data_cadastro  as dataCadastroProduto \r\n"
				+ "  from tb_cliente_produto cp \r\n"
				+ "  Inner join tb_cliente c on cp.cliente_id = c.id \r\n"
				+ "  Inner join tb_produto p on cp.produto_id = p.id \r\n"
				+ "");
		TypedQuery<ClienteProdutoDto> query = manager.createQuery(jpql.toString(), ClienteProdutoDto.class);
		
		List<ClienteProdutoDto> results = query.getResultList();
		
		return results;
	}
}
