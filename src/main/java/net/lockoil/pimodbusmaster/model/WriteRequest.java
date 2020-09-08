package net.lockoil.pimodbusmaster.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Запрос на запись регистров
 */
@Data
@AllArgsConstructor
public class WriteRequest {
	/**
	 * Адрес устройства
	 */
	private int slave;
	/**
	 * Адрес начального регистра
	 */
	private int address;
	/**
	 * Массив из записываемых значений
	 */
	private int[] values;
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
