package net.lockoil.pimodbusmaster.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import net.lockoil.pimodbusmaster.model.SubDevice;

public interface SubDeviceRepository extends JpaRepository<SubDevice, Long> {
	
	List<SubDevice> findByAddressAndDeviceId(Integer address, Long deviceId);	
	List<SubDevice> findAllByDeviceId(Long deviceId);
}

