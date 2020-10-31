package net.lockoil.pimodbusmaster.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import net.lockoil.pimodbusmaster.model.CardRegisterElement;

/*
 * Репозиторий для работы с регистрами modbus-устройства
 */
public interface RegistersRepository extends CrudRepository<CardRegisterElement, Long> {
	
	List<CardRegisterElement> findBySubDeviceId(Long id);
	List<CardRegisterElement> findByAddressAndSubDeviceId(Integer address, Long deviceId);
	

}