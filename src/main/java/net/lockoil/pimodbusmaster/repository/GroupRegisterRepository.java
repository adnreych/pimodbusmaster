package net.lockoil.pimodbusmaster.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import net.lockoil.pimodbusmaster.model.RegisterGroup;

/**
 * Репозиторий для группы регистров
 */
public interface GroupRegisterRepository extends JpaRepository<RegisterGroup, Long> {

}
