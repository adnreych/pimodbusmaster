package net.lockoil.pimodbusmaster.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.lockoil.pimodbusmaster.model.Device;
import net.lockoil.pimodbusmaster.model.SubDevice;
import net.lockoil.pimodbusmaster.repository.SubDeviceRepository;

@Service
public class SubDeviceService {
	
	private final SubDeviceRepository subDeviceRepository;
	private final Logger log = Logger.getLogger(this.getClass().getSimpleName());

	@Autowired
	public SubDeviceService(SubDeviceRepository subDeviceRepository) {
		this.subDeviceRepository = subDeviceRepository;
	}
	
	
	public List<SubDevice> findAll() {
		return subDeviceRepository.findAll();
	}
	
	
	public SubDevice getByAddressAndDeviceId(Integer address, Long deviceId) {
		List<SubDevice> subDevices = subDeviceRepository.findByAddressAndDeviceId(address, deviceId);
		if (!subDevices.isEmpty()) {
			return subDevices.get(0);
		} else {
			return null;
		}
		
	}
	
	public SubDevice save(SubDevice subDevice) {
		return subDeviceRepository.save(subDevice);
	}

}
