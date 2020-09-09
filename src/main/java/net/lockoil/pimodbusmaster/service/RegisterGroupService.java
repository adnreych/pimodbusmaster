package net.lockoil.pimodbusmaster.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.lockoil.pimodbusmaster.exceptions.DeviceNotFoundException;
import net.lockoil.pimodbusmaster.model.Device;
import net.lockoil.pimodbusmaster.model.RegisterGroup;
import net.lockoil.pimodbusmaster.model.RegisterGroupResource;
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
	
	public List<RegisterGroupResource> saveAll(List<RegisterGroup> registerGroups) {
		List<RegisterGroup> regGroups = groupRegisterRepository.saveAll(registerGroups);
		List<RegisterGroupResource> regGroupsResources = regGroups.stream().map(e -> new RegisterGroupResource(e.getId(), e.getName())).collect(Collectors.toList());
		return regGroupsResources;
	}
	
	public RegisterGroup findById(Long id) {
		return groupRegisterRepository.findById(id).orElseThrow(NoSuchElementException::new);
	}
	
	public List<RegisterGroup> findAll() {
		return groupRegisterRepository.findAll();
	}
}
