package net.lockoil.pimodbusmaster.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Сущность группы регистров, возвращаемая по API
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterGroupResource {
	
	/**
	 * id группы регистров
	 */
	private Long id;
	
	/**
	 * Имя группы
	 */
	private String name;

}
