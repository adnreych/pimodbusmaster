package net.lockoil.pimodbusmaster.model.modbustypes;

import java.util.List;

import lombok.Data;

@Data
public class BitTypeLegend implements TypeSupportable {
	private Integer startBit;
	private Integer bitQuantity;
	private List<String> possibleValues;	
	private String description;
}
