package net.lockoil.pimodbusmaster.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReadRequest {
	private int slave;
	private int address;
	private int count;
	private String type;
	
}
