package net.lockoil.pimodbusmaster.model.modbustypes;

import java.util.List;

import lombok.Data;

@Data
public class VarTypeLegend implements TypeSupportable {
	Integer value;
	String description;
}
