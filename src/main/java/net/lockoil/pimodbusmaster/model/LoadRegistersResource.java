package net.lockoil.pimodbusmaster.model;

import java.util.List;
import java.util.Map;

import javax.persistence.Column;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.lockoil.pimodbusmaster.model.modbustypes.TypeSupportable;

@Data
@AllArgsConstructor
public class LoadRegistersResource {
	
	private Long id;
	private Long device;
	private String name;
	private Integer address;
	private Integer count;
	private Boolean isRead;
	private Boolean isWrite;
	private String type;
	private String suffix;
	private Long multiplier;
	private Long minValue;
	private Long maxValue;
	private String group;
	private List<TypeSupportable> legends;

}
