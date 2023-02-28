package br.com.unipix.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.unipix.api.model.ClienteProduto;

@Repository
public interface ClienteProdutoRepository extends JpaRepository<ClienteProduto, Long>{

	@Query(value="SELECT a.* FROM unipix.tb_cliente_produto a where a.cliente_id=?1 and a.produto_id=?2", nativeQuery=true)
	ClienteProduto findByAllClienteProduto(Long clienteId, Long produtoId);

	@Query(value="SELECT a.* FROM unipix.tb_cliente_produto a where a.cliente_id=?1", nativeQuery=true)
	List<ClienteProduto> findByAllClienteProdutoList(Long clienteId);

}

