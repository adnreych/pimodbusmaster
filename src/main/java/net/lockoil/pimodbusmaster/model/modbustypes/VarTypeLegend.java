package net.lockoil.pimodbusmaster.model.modbustypes;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

import lombok.Data;

@JsonTypeInfo(
	    use=JsonTypeInfo.Id.NAME,
	    include=JsonTypeInfo.As.PROPERTY,
	    property="type")
@JsonTypeName("varType")
@Data
public class VarTypeLegend implements TypeSupportable {
	private Integer value;
	private String description;
	
	public VarTypeLegend() {};
}
