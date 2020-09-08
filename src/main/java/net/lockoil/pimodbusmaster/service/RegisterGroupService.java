package net.lockoil.pimodbusmaster.service;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.lockoil.pimodbusmaster.exceptions.DeviceNotFoundException;
import net.lockoil.pimodbusmaster.model.RegisterGroup;
import net.lockoil.pimodbusmaster.repository.GroupRegisterRepository;

/**
 * Сервис для группы регистров
 */
@Service
public class RegisterGroupService {
	
	private final GroupRegisterRepository groupRegisterRepository;	
	
	@Autowired
	public RegisterGroupService(GroupRegisterRepository groupRegisterRepository) {
		this.groupRegisterRepository = groupRegisterRepository;
	}
	
	public RegisterGroup save(RegisterGroup registerGroup) {
		return groupRegisterRepository.save(registerGroup);
	}
	
	public RegisterGroup findById(Long id) {
		Optional<RegisterGroup> registerGroup = groupRegisterRepository.findById(id);
		return registerGroup.orElseThrow(NoSuchElementException::new);
	}
}
