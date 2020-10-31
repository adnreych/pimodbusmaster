package net.lockoil.pimodbusmaster.model.modbustypes;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

import lombok.Data;

/*
 * Элемент описания типа {@link BitTypeModbus}
 */
@JsonTypeInfo(
	    use=JsonTypeInfo.Id.NAME,
	    include=JsonTypeInfo.As.PROPERTY,
	    property="type")
@JsonTypeName("bitType")
@Data
public class BitTypeLegend implements TypeSupportable {
	private Integer startBit;
	private Integer bitQuantity;
	private List<String> possibleValues;	
	private String description;
	
	public BitTypeLegend() {};
}
