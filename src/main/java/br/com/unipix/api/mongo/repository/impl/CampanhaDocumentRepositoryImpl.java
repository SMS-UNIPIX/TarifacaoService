package br.com.unipix.api.mongo.repository.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOptions;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import br.com.unipix.api.mongo.model.CampanhaDocument;
import br.com.unipix.api.mongo.model.SmsConsumoSumarizado;
import br.com.unipix.api.mongo.repository.CampanhaDocumentRepository;

@Repository
public class CampanhaDocumentRepositoryImpl implements CampanhaDocumentRepository {

    @Autowired
    private MongoTemplate mongoTemplate;
    
    @Override
    public List<CampanhaDocument> obterVendaProdutoClientePeriodoArquivo(Date dataInicial, Date dataFinal, Long clienteId) {
        Criteria query = new Criteria();
        
        query.and("dataEnvio").gte(dataInicial).lte(dataFinal);
        query.and("clienteId").is(clienteId);
        query.and("situacao").is("Ativo");
        query.and("status").in("enviado para a operadora", "não entregue", "entregue no aparelho", "falha");
        
        MatchOperation matchFilters = Aggregation.match(query);

        GroupOperation group = Aggregation.group("clienteId", "clienteNome", "produtoId", "produtoNome")
                .sum("quantidadeEnvios").as("quantidadeEnvios");

        ProjectionOperation projection = Aggregation
                .project("clienteId", "clienteNome", "produtoId", "produtoNome", "quantidadeEnvios");
        
		AggregationOptions options = AggregationOptions.builder().allowDiskUse(true).build();
        Aggregation aggregation = Aggregation.newAggregation(matchFilters, group, projection).withOptions(options);

        AggregationResults<CampanhaDocument> dados = mongoTemplate.aggregate(aggregation, CampanhaDocument.class, CampanhaDocument.class);

        return dados.getMappedResults();
    }
    
    @Override
    public void removerRegistrosDia(Date dataInicial, Date dataFinal) {
        Query query = new Query();
        query.addCriteria(Criteria.where("dia").gte(dataInicial).lte(dataFinal));
        mongoTemplate.remove(query, SmsConsumoSumarizado.class, "smsConsumoSumarizado");
    }

    @Override
    public void salvar(List<SmsConsumoSumarizado> sumarizados) {
        BulkOperations b = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, SmsConsumoSumarizado.class);
        sumarizados.forEach(b::insert);
        b.execute();
    }

}
