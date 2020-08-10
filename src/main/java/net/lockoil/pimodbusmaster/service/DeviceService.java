package net.lockoil.pimodbusmaster.service;

import java.util.List;
import java.util.Optional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jssc.SerialPortException;
import net.lockoil.pimodbusmaster.csd.ATConnect;
import net.lockoil.pimodbusmaster.exceptions.DeviceNotFoundException;
import net.lockoil.pimodbusmaster.model.AtConnectionRequest;
import net.lockoil.pimodbusmaster.model.Device;
import net.lockoil.pimodbusmaster.repository.DeviceRepository;

@Service
public class DeviceService {
	
	private final Logger log = Logger.getLogger(this.getClass().getSimpleName());
	
	private final DeviceRepository deviceRepository;
	
	@Autowired
	private ATConnect atConnect;
	
	@Autowired
	private RegistersService loadNewCardService;

	@Autowired
	public DeviceService(DeviceRepository deviceRepository) {
		this.deviceRepository = deviceRepository;
	}
	
	public Device save(Device device) {
		return deviceRepository.save(device);
	}
	
	public Device findById(Long id) throws DeviceNotFoundException {
		Optional<Device> device = deviceRepository.findById(id);
		return device.orElseThrow(DeviceNotFoundException::new);
	}
	
	public List<Device> findAll() {
		return deviceRepository.findAll();
	}
	
	public Device findByAddress(Integer address) {
		return Optional.of(deviceRepository.findByAddress(address).get(0)).get();
	}
	
	@Transactional
	public void delete(Long id) {
		loadNewCardService.deleteByDeviceId(id);
		deviceRepository.deleteById(id);
	}
	
	public String atConnect(AtConnectionRequest atConnectionRequest) throws SerialPortException {
		return atConnect.getAtConnection(atConnectionRequest);
	}
	
	public boolean atDisconnect(AtConnectionRequest atConnectionRequest) throws SerialPortException {
		return atConnect.closePort(atConnectionRequest);
	}
	
	public void refreshConnection(AtConnectionRequest atConnectionRequest) throws SerialPortException {
		atConnect.refreshConnectionStatus(atConnectionRequest);
	}
	
}
