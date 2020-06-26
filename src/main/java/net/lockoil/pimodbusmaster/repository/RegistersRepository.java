package net.lockoil.pimodbusmaster.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import net.lockoil.pimodbusmaster.model.CardRegisterElement;


public interface RegistersRepository extends CrudRepository<CardRegisterElement, Long> {
	
	List<CardRegisterElement> findByDeviceId(Long id);

}