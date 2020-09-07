package net.lockoil.pimodbusmaster.model;

import lombok.Data;

/**
 * Запрос AT-соединения
 */
@Data
public class AtConnectionRequest {
	/**
	 * Номер телефона
	 */
	private String phone;
	/**
	 * Название COM-порта. Обычно COM1-COM4 для Windows и /dev/ttyUSB0 для Linux
	 */
	private String port;
}
