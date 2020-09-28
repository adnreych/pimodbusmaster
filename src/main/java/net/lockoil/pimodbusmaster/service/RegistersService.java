package net.lockoil.pimodbusmaster.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.lockoil.pimodbusmaster.model.CardRegisterElement;
import net.lockoil.pimodbusmaster.model.LoadRegistersResource;
import net.lockoil.pimodbusmaster.model.SubDevice;
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
		List<SubDevice> subDevices = subDeviceService.getAllByDeviceId(id);	
		return subDevices
				.stream()
				.map(e -> registerRepository.findBySubDeviceId(e.getId()))
				.flatMap(Collection::stream)
				.collect(Collectors.toList());
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
		return Optional.of(registerRepository.findByAddressAndSubDeviceId(registerAddress, deviceId).get(0)).get();
	}
	
	public CardRegisterElement parseRegisterElement(LoadRegistersResource loadRegistersResource) {
		
		String name = loadRegistersResource.getName();
		Integer address = loadRegistersResource.getAddress();
		Integer count = loadRegistersResource.getCount();
		Boolean isWrite = loadRegistersResource.getIsWrite();
		Boolean isRead = loadRegistersResource.getIsRead();
		Integer readFunction = loadRegistersResource.getReadFunction();
		Integer writeFunction = loadRegistersResource.getWriteFunction();
		String type = loadRegistersResource.getType();
		String suffix = loadRegistersResource.getSuffix() != null ? loadRegistersResource.getSuffix() : null;
		Long min = loadRegistersResource.getMinValue() != null ? loadRegistersResource.getMinValue() : null;
		Long max = loadRegistersResource.getMaxValue() != null ? loadRegistersResource.getMaxValue() : null;
		Long multiplier = loadRegistersResource.getMultiplier() != null ? loadRegistersResource.getMultiplier() : null;
		SubDevice subDevice = loadRegistersResource.getSubDevice();
		String legends = loadRegistersResource.getLegends() != null ? loadRegistersResource.getLegends() : null;
		String registerGroup = loadRegistersResource.getRegisterGroup() != null ? loadRegistersResource.getRegisterGroup() : null;
		

		SubDevice subDeviceFromDb = subDeviceService.getByAddressAndDeviceId(subDevice.getAddress(), subDevice.getDevice().getId());
		if (subDeviceFromDb == null) {
			subDeviceFromDb = subDeviceService.save(subDevice);
		}
		
		
		CardRegisterElement cardRegisterElement = new CardRegisterElement();
		
		if (loadRegistersResource.getId() != null) cardRegisterElement.setId(loadRegistersResource.getId());
		cardRegisterElement.setName(name);
		cardRegisterElement.setAddress(address);
		cardRegisterElement.setCount(count);
		cardRegisterElement.setIsRead(isRead);
		cardRegisterElement.setIsWrite(isWrite);
		cardRegisterElement.setReadFunction(readFunction);
		cardRegisterElement.setWriteFunction(writeFunction);
		cardRegisterElement.setType(type);
		cardRegisterElement.setSuffix(suffix);
		cardRegisterElement.setMinValue(min);
		cardRegisterElement.setMaxValue(max);
		cardRegisterElement.setMultiplier(multiplier);
		cardRegisterElement.setSubDevice(subDeviceFromDb);;
		cardRegisterElement.setLegends(legends);
		cardRegisterElement.setRegisterGroup(registerGroup);
		
		
		return cardRegisterElement;
		
	}

}
