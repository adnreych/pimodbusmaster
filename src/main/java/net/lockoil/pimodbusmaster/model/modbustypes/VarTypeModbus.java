package net.lockoil.pimodbusmaster.model.modbustypes;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/*
 * Тип, значение элемента массива которого соответствует конкретному описанию 
 */
public class VarTypeModbus implements AbstractModbusType<Integer, Integer> {

	private Integer value;
	private List<VarTypeLegend> legends;
	
	public VarTypeModbus(List<VarTypeLegend> legends, Integer value) {
		this.legends = legends;
		this.value = value;
	}
	
	@Override
	public Integer readValue() {
		return legends
				.stream()
				.filter(entry -> Objects.equals(entry.getValue(), value))
				.findFirst()
				.get()
				.getValue();
	}

	@Override
	public void writeValue(Integer value) {
		this.value = value;	
	}
	
	public Integer getValue() {
		return value;
	}
		

}
