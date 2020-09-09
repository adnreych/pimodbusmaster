package net.lockoil.pimodbusmaster.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

/**
 * Группа регистров
 */
@Entity
@Data
@Table(name = "register_group")
public class RegisterGroup {
	
	/**
	 * id группы регистров
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, insertable = false)
	private Long id;
	
	/**
	 * Имя группы
	 */
	@Column(name = "group_name")
	private String name;
	
	/**
	 * Регистры {@link CardRegisterElement} входящие в группу
	 */
	@OneToMany(mappedBy = "registerGroup", cascade = CascadeType.ALL)
	private Set<CardRegisterElement> cardRegisterElements;

}
