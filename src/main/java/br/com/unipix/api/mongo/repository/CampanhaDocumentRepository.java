package br.com.unipix.api.mongo.repository;

import java.util.Date;
import java.util.List;

import br.com.unipix.api.mongo.model.CampanhaDocument;
import br.com.unipix.api.mongo.model.SmsConsumoSumarizado;

public interface CampanhaDocumentRepository {

    List<CampanhaDocument> obterVendaProdutoClientePeriodoArquivo(Date dataInicial, Date dataFinal, Long clienteId);

    void removerRegistrosDia(Date dataInicial, Date dataFinal);
    
    void salvar(List<SmsConsumoSumarizado> sumarizados);
    
}
