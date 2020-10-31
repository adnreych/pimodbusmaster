package net.lockoil.pimodbusmaster.exceptions;

/*
 * Запрашиваемое устройство не найдено
 */
public class DeviceNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	public DeviceNotFoundException() {
        super("");
    }
}
