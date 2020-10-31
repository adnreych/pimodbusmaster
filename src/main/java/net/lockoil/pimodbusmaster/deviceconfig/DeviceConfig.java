package net.lockoil.pimodbusmaster.deviceconfig;

import com.intelligt.modbus.jlibmodbus.serial.SerialParameters;
import com.intelligt.modbus.jlibmodbus.serial.SerialPort;
import com.intelligt.modbus.jlibmodbus.serial.SerialPortFactoryJSSC;
import com.intelligt.modbus.jlibmodbus.serial.SerialUtils;

/**
 * Возвращает параметры COM-porta, необходимые для текущего подключения
 */
public class DeviceConfig {
	
	/**
	 * 
	 * @return Параметры для соединения через Raspberry Pi (OS Linux)
	 */
	public static SerialParameters getByRPiConnection() {
		SerialParameters sp = new SerialParameters();
        sp.setDevice("/dev/ttyUSB0");
        sp.setBaudRate(SerialPort.BaudRate.BAUD_RATE_19200);
        sp.setDataBits(8);
        sp.setParity(SerialPort.Parity.NONE);
        sp.setStopBits(1);
        SerialUtils.setSerialPortFactory(new SerialPortFactoryJSSC());
        return sp;
	}

}
