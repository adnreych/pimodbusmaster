package net.lockoil.pimodbusmaster.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import net.lockoil.pimodbusmaster.model.CardRegisterElement;


public interface RegistersRepository extends CrudRepository<CardRegisterElement, Long> {
	
	List<CardRegisterElement> findByDeviceId(Long id);
	List<CardRegisterElement> findByAddressAndDeviceId(Integer address, Long deviceId);
	

}