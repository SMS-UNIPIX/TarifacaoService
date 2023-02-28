package br.com.unipix.api.mongo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.unipix.api.mongo.model.CampanhaDashboard;

@Repository
public interface CampanhaDashboardRepository extends MongoRepository<CampanhaDashboard, String> {

	@Query(value = "{'idCampanhaSql' : ?0}")
	CampanhaDashboard findByCampaingId(Long campanhaId);
		
}
