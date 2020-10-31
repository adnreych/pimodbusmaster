package net.lockoil.pimodbusmaster.exceptions;

/*
 * Ошибка, которая возникает в случае когда реализация {@link AbstractModbusType} неизвестна
 */
public class IllegalModbusTypeException extends Exception {

	private static final long serialVersionUID = 1L;

	public IllegalModbusTypeException() {
        super("");
    }
}