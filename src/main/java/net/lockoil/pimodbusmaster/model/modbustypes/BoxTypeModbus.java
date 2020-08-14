package net.lockoil.pimodbusmaster.model.modbustypes;

import java.util.List;
import java.util.stream.Collectors;

public class BoxTypeModbus implements AbstractModbusType<Integer, List<Object>> {
	
	private List<AbstractModbusType> content;
	private Integer value;	

	public BoxTypeModbus(List<AbstractModbusType> content, Integer value) {
		this.content = content;
		this.value = value;
	}

	@Override
	public List<Object> readValue() {
		return content
				.stream()
				.map(e -> e.readValue())
				.collect(Collectors.toList());
	}

	@Override
	public void writeValue(Integer value) {
		this.value = value;	
	}


}
