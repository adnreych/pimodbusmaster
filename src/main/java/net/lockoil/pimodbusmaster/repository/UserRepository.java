package net.lockoil.pimodbusmaster.repository;

import org.springframework.data.repository.CrudRepository;

import net.lockoil.pimodbusmaster.model.User;

public interface UserRepository extends CrudRepository<User, Long> {
	User findByUsername(String username);
}