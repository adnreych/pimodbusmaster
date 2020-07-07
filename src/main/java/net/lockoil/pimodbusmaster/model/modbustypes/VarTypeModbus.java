package net.lockoil.pimodbusmaster.model.modbustypes;

import java.util.Map;
import java.util.Objects;

public class VarTypeModbus implements AbstractModbusType<Integer, String> {

	private Integer value;
	private Map<String, Integer> legend;
	
	public VarTypeModbus(Map<String, Integer> legend, Integer value) {
		this.legend = legend;
		this.value = value;
	}
	
	@Override
	public String readValue() {
		return legend
				.entrySet()
				.stream()
				.filter(entry -> Objects.equals(entry.getValue(), value))
				.findFirst()
				.get()
				.getKey();
	}

	@Override
	public void writeValue(Integer value) {
		this.value = value;	
	}
	
	public Integer getValue() {
		return value;
	}
		

}
