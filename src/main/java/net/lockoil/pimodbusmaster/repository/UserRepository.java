package net.lockoil.pimodbusmaster.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import net.lockoil.pimodbusmaster.model.User;

public interface UserRepository extends CrudRepository<User, Long> {
	User findByUsername(String username);
}