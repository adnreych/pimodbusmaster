package net.lockoil.pimodbusmaster.model;

import java.util.Map;

import javax.persistence.Column;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoadRegistersResource {
	
	private Long deviceId;
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

}
