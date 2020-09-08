package net.lockoil.pimodbusmaster.model;

import lombok.Data;

/**
 * Ответ на чтение регистров
 */
@Data
public class ReadResponse {
	
	/**
	 * Адрес регистра
	 */
	int address;
	/**
	 * Значение, связанное с адресом регистра
	 */
	int value;
	
	public ReadResponse(int address, int value) {
		this.address = address;
		this.value = value;
	}
	
	@Override
	public String toString() {
		return "Адрес: " + address + " Значение: " + value + System.lineSeparator();
	}
}
