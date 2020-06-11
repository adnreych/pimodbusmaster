package net.lockoil.pimodbusmaster.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WriteRequest {
	private int slave;
	private int address;
	private int[] values;
}
