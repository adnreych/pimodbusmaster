package net.lockoil.pimodbusmaster.model;

import java.util.List;
import java.util.Map;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonRawValue;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.lockoil.pimodbusmaster.model.modbustypes.TypeSupportable;

/**
 * Описание регистра для загрузки в БД. Описание всех полей аналогично {@link CardRegisterElement}
 */
@Data
@NoArgsConstructor
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
	private String registerGroup;
	@JsonRawValue
	private String legends;

}
