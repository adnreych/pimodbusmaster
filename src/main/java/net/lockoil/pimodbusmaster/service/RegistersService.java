package net.lockoil.pimodbusmaster.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.lockoil.pimodbusmaster.exceptions.DeviceNotFoundException;
import net.lockoil.pimodbusmaster.model.CardRegisterElement;
import net.lockoil.pimodbusmaster.model.Device;
import net.lockoil.pimodbusmaster.model.LoadRegistersResource;
import net.lockoil.pimodbusmaster.repository.RegistersRepository;

@Service
public class RegistersService {
	
	private final Logger log = Logger.getLogger(this.getClass().getSimpleName());
	
	private final RegistersRepository registerRepository;
	
	@Autowired
	private DeviceService deviceService;
	
	@Autowired
	public RegistersService(RegistersRepository registerRepository) {
		this.registerRepository = registerRepository;
	}
	
	public ArrayList<CardRegisterElement> saveRegisters(ArrayList<CardRegisterElement> cardRegisterElements) {
		registerRepository.saveAll(cardRegisterElements);
		return cardRegisterElements;
	}
	
	public List<CardRegisterElement> getDeviceRegisters(Long id) {
		return registerRepository.findByDeviceId(id);
	}
	
	
	public CardRegisterElement parseRegisterElement(LoadRegistersResource loadRegistersResource) {
		
		Long deviceId = loadRegistersResource.getDevice();
		String name = loadRegistersResource.getName();
		Integer address = loadRegistersResource.getAddress();
		Integer count = loadRegistersResource.getCount();
		Boolean isWrite = loadRegistersResource.getIsWrite();
		Boolean isRead = loadRegistersResource.getIsRead();
		String type = loadRegistersResource.getType();
		String suffix = loadRegistersResource.getSuffix() != null ? loadRegistersResource.getSuffix() : null;
		Long min = loadRegistersResource.getMinValue() != null ? loadRegistersResource.getMinValue() : null;
		Long max = loadRegistersResource.getMaxValue() != null ? loadRegistersResource.getMaxValue() : null;
		Long multiplier = loadRegistersResource.getMultiplier() != null ? loadRegistersResource.getMultiplier() : null;
		
		CardRegisterElement cardRegisterElement = new CardRegisterElement();
		Device device;
		
		try {
			device = deviceService.findById(deviceId);		
			cardRegisterElement.setDevice(device);
			cardRegisterElement.setName(name);
			cardRegisterElement.setAddress(address);
			cardRegisterElement.setCount(count);
			cardRegisterElement.setIsRead(isRead);
			cardRegisterElement.setIsWrite(isWrite);
			cardRegisterElement.setType(type);
			cardRegisterElement.setSuffix(suffix);
			cardRegisterElement.setMinValue(min);
			cardRegisterElement.setMaxValue(max);
			cardRegisterElement.setMultiplier(multiplier);
		} catch (DeviceNotFoundException e) {
			e.printStackTrace();
		}
		
		
		return cardRegisterElement;
		
	}

}
