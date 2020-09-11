package net.lockoil.pimodbusmaster.model.modbustypes;

import java.util.List;

import net.lockoil.pimodbusmaster.model.ReadRequest;
import net.lockoil.pimodbusmaster.model.ReadResponse;

/**
 * Интерфейс для работы с различными типами регистров modbus.
 * При реализации интерфейса новый тип нужно добавить в метод
 * {@link net.lockoil.pimodbusmaster.util.ModbusTypeParser#parseRead(List<ReadResponse>, ReadRequest) parseRead}
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
