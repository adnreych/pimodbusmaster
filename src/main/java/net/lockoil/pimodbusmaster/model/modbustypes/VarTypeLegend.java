package net.lockoil.pimodbusmaster.model.modbustypes;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

import lombok.Data;

/*
 * Элемент описания типа {@link VarTypeModbus}
 */
@JsonTypeInfo(
	    use=JsonTypeInfo.Id.NAME,
	    include=JsonTypeInfo.As.PROPERTY,
	    property="type")
@JsonTypeName("varType")
@Data
public class VarTypeLegend implements TypeSupportable {
	private Integer value;
	private String description;
	
	public VarTypeLegend() {}
	
	public VarTypeLegend(Integer value, String description) {
		this.value = value;
		this.description = description;
	};
	
	
}
