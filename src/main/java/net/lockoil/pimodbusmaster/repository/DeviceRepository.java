package net.lockoil.pimodbusmaster.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import net.lockoil.pimodbusmaster.model.Device;

/*
 * Репозиторий для работы с modbus-устройствами
 */
public interface DeviceRepository extends JpaRepository<Device, Long> {
		
}
