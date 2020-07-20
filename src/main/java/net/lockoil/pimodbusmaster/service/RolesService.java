package net.lockoil.pimodbusmaster.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import net.lockoil.pimodbusmaster.model.Role;
import net.lockoil.pimodbusmaster.repository.RoleRepository;

@Service
public class RolesService {
	
	private final RoleRepository roleRepository;
	
	public RolesService(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}
	
	public List<Role> findAll() {
		return Lists.newArrayList(roleRepository.findAll());
	}
}
