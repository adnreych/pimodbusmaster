package net.lockoil.pimodbusmaster.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.lockoil.pimodbusmaster.exceptions.DeviceNotFoundException;
import net.lockoil.pimodbusmaster.model.CardRegisterElement;
import net.lockoil.pimodbusmaster.model.Device;
import net.lockoil.pimodbusmaster.model.LoadRegistersResource;
import net.lockoil.pimodbusmaster.model.RegisterGroup;
import net.lockoil.pimodbusmaster.model.RegisterGroupResource;
import net.lockoil.pimodbusmaster.model.SubDevice;
import net.lockoil.pimodbusmaster.model.modbustypes.TypeSupportable;
import net.lockoil.pimodbusmaster.repository.RegistersRepository;

/**
 * Сервис для работы с сущностями регистров
 */
@Service
public class RegistersService {
	
	private final Logger log = Logger.getLogger(this.getClass().getSimpleName());
	
	private final RegistersRepository registerRepository;
	
	@Autowired
	private DeviceService deviceService;
	
	@Autowired
	private RegisterGroupService registerGroupService;
	
	@Autowired
	private SubDeviceService subDeviceService;
	
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
	
	public void deleteByDeviceId(Long id) {
		List<CardRegisterElement> cardRegisterElements = new ArrayList<>();
		cardRegisterElements = getDeviceRegisters(id);
		registerRepository.deleteAll(cardRegisterElements);
	}
	
	public void changeRegister(CardRegisterElement cardRegisterElement) {
		registerRepository.save(cardRegisterElement);
	}
	
	public void deleteRegister(Long id) {
		registerRepository.deleteById(id);
	}
	
	public Long addRegister(CardRegisterElement cardRegisterElement) {
		CardRegisterElement element = registerRepository.save(cardRegisterElement);
		return element.getId();
	}
	
	@Transactional
	public CardRegisterElement getRegister(Long deviceId, Integer registerAddress) {
		return Optional.of(registerRepository.findByAddressAndDeviceId(registerAddress, deviceId).get(0)).get();
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
		SubDevice subDevice = loadRegistersResource.getSubDevice();
		String legends = loadRegistersResource.getLegends() != null ? loadRegistersResource.getLegends() : null;
		
		RegisterGroup registerGroup = null;
		
		if (loadRegistersResource.getRegisterGroup() != null) {
			String registerGroupId = loadRegistersResource.getRegisterGroup();
			registerGroup = registerGroupService.findById(Long.valueOf(registerGroupId));
		}

		SubDevice subDeviceFromDb = subDeviceService.getByAddressAndDeviceId(subDevice.getAddress(), subDevice.getDeviceId());
		if (subDeviceFromDb == null) {
			subDeviceFromDb = subDeviceService.save(subDevice);
		}
		
		
		CardRegisterElement cardRegisterElement = new CardRegisterElement();
		Device device;
		
		try {
			if (loadRegistersResource.getId() != null) cardRegisterElement.setId(loadRegistersResource.getId());
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
			cardRegisterElement.setSubDevice(subDeviceFromDb);;
			cardRegisterElement.setLegends(legends);
			cardRegisterElement.setRegisterGroup(registerGroup);
		} catch (DeviceNotFoundException e) {
			e.printStackTrace();
		} 
		
		
		return cardRegisterElement;
		
	}

}
