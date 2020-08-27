package net.lockoil.pimodbusmaster.model.modbustypes;

import org.springframework.data.util.Pair;

public class BoxTypeModbus implements AbstractModbusType<Integer, Pair<Object, Object>> {
	
	private Pair<AbstractModbusType, AbstractModbusType> content;
	private Integer value;	

	public BoxTypeModbus(Pair<AbstractModbusType, AbstractModbusType>  content) {
		this.content = content;
	}

	@Override
	public Pair<Object, Object> readValue() {
		return Pair.of(content.getFirst().readValue(), content.getSecond().readValue());
	}

	@Override
	public void writeValue(Integer value) {
		this.value = value;	
	}

}
