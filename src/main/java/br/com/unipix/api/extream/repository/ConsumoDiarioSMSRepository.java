package br.com.unipix.api.extream.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import br.com.unipix.api.extream.model.ConsumoDiarioSMS;

@Transactional
public interface ConsumoDiarioSMSRepository extends JpaRepository<ConsumoDiarioSMS, Long>{

	@Modifying
	@Query(value = "DELETE from ConsumoDiarioSMS WHERE dia BETWEEN ?1 AND ?2", nativeQuery = true)
	void deletarConsumoSMS(LocalDateTime dataInicia, LocalDateTime dataFinal);

}
