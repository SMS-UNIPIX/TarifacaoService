package br.com.unipix.api.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.unipix.api.model.HistoricoPrecoProduto;

@Repository
public interface HistoricoPrecoProdutoRepository extends JpaRepository<HistoricoPrecoProduto, Long>{

	@Query(value="SELECT a.* FROM unipix.tb_cliente_produto a where a.cliente_id=?1 and a.produto_id=?2", nativeQuery=true)
	HistoricoPrecoProduto findByAllClienteProduto(Long clienteId, Long produtoId);

	@Query(value="SELECT a.* FROM unipix.tb_historico_preco_produto a where a.data_inicio<=?1", nativeQuery=true)
	List<HistoricoPrecoProduto> findByAllDataAgendadaDiat(LocalDateTime dataAgendadaDia);

}

