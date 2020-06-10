package net.lockoil.pimodbusmaster.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ModbusReadRequest {
	private int slave;
	private int address;
	private int count;
	
}
