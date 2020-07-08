package net.lockoil.pimodbusmaster.model.modbustypes;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
		  use = JsonTypeInfo.Id.NAME, 
		  include = JsonTypeInfo.As.PROPERTY, 
		  property = "type")
@JsonSubTypes({ 
	  @Type(value = VarTypeLegend.class, name = "varType"), 
	  @Type(value = BitTypeLegend.class, name = "bitType") 
	})
public interface TypeSupportable {

}
