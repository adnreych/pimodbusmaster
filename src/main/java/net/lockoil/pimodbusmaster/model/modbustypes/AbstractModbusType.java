package net.lockoil.pimodbusmaster.model.modbustypes;

/**
 * Интерфейс для работы с различными типами регистров modbus
 */
public interface AbstractModbusType<Input, Output> {
	
	/**
	 * Возвращает результат чтения значения в заданном в Output виде
	 */	
	Output readValue();
	
	/**
	 * Готовит заданное значение к записи в контроллер (Input - это почти всегда Integer)
	 */	
	void writeValue(Input value);
}
