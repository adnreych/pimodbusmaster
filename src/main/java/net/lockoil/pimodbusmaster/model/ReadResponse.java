package net.lockoil.pimodbusmaster.model;

import lombok.Data;

@Data
public class ReadResponse {
	
	int address;
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
