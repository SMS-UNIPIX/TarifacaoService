package br.com.unipix.api.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.unipix.api.enumaration.StatusHistoricoEnum;
import br.com.unipix.api.model.HistoricoPrecoProduto;

@Repository
public interface HistoricoPrecoProdutoRepository extends JpaRepository<HistoricoPrecoProduto, Long>{

	@Query(value="SELECT a.* FROM unipix.tb_historico_preco_produto a where a.cliente_id=?1 and a.produto_id=?2 and a.status=?3 order by data_inicio DESC", nativeQuery=true)
	List<HistoricoPrecoProduto> findByAllClienteProdutoStatus(Long clienteId, Long produtoId, StatusHistoricoEnum status);

	@Query(value="SELECT a.* FROM unipix.tb_historico_preco_produto a where a.data_inicio<=?1 and a.status=2", nativeQuery=true)
	List<HistoricoPrecoProduto> findByAllDataAgendadaDia(LocalDateTime dataAgora);

}

