package net.lockoil.pimodbusmaster.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Запрос на чтение регистров
 */
@Data
@AllArgsConstructor
public class ReadRequest {
	/**
	 * Адрес устройства
	 */
	private int slave;
	/**
	 * Адрес регистра
	 */
	private int address;
	/**
	 * Число регистров
	 */
	private int count;
	/**
	 * Тип регистра
	 */
	private String type;
	/**
	 * true - если это соединение по CSD
	 */
	boolean isCSD;
	/**
	 * Не равно null, если {@link isCSD} true
	 */
	AtConnectionRequest atConnectionRequest;
}
