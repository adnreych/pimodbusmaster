package net.lockoil.pimodbusmaster.model.modbustypes;


public interface AbstractModbusType<Input, Output> {
	Output readValue();
	void writeValue(Input value);
}
