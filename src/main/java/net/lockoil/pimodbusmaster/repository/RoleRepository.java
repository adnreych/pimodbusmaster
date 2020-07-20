package net.lockoil.pimodbusmaster.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import net.lockoil.pimodbusmaster.model.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {
	
	Optional<List<Role>> findByName(String name);
	
}
