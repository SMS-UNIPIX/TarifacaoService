package br.com.unipix.api.mongo.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.unipix.api.mongo.model.RespostaMensagem;

@Repository
public interface RespostaRepository extends MongoRepository<RespostaMensagem, String> {

	@Aggregation({ "{$match:{ idCliente:?0, data: {$gte: ?1, $lte: ?2}}}",
		"{$group:{ _id:{ idCliente:'$idCliente', idProduto: '$idProduto'}, quantidade:{$sum: 1}}}",
		"{$project:{ idCliente: 1, idProduto: 1, quantidade: 1}}" })
    List<RespostaMensagem> countRespostaMensagemByCliente(
            Long idCliente,
            Date dataInicio,
            Date dataFim
    );

    @Query(value = "{'idCliente': ?0, tarifa: {$nin: [null,'']}, 'data': { $gte: ?1, $lte: ?2} }")
    List<RespostaMensagem> countRespostaMensagemByCliente1(
            Long idCliente,
            Date dataInicio,
            Date dataFim
    );
    
    @Query(value = "{'data': { $gte: ?0, $lte: ?1}, 'idProduto': {$eq:null}}")
    List<RespostaMensagem> respostaMensagemByProduto(
            Date dataInicio,
            Date dataFim
    );
}
