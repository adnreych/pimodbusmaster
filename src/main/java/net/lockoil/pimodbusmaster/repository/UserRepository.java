package net.lockoil.pimodbusmaster.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import net.lockoil.pimodbusmaster.model.User;

/*
 * Репозиторий для работы с пользователями
 */
public interface UserRepository extends CrudRepository<User, Long> {
	
	User findByUsername(String username);
	List<User> findAll();
}