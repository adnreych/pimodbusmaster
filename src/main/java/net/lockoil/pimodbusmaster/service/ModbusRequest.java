package net.lockoil.pimodbusmaster.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.intelligt.modbus.jlibmodbus.Modbus;
import com.intelligt.modbus.jlibmodbus.exception.ModbusIOException;
import com.intelligt.modbus.jlibmodbus.master.ModbusMaster;
import com.intelligt.modbus.jlibmodbus.master.ModbusMasterFactory;
import com.intelligt.modbus.jlibmodbus.serial.SerialParameters;
import com.intelligt.modbus.jlibmodbus.serial.SerialPort;
import com.intelligt.modbus.jlibmodbus.serial.SerialPortException;
import com.intelligt.modbus.jlibmodbus.serial.SerialPortFactoryJSSC;
import com.intelligt.modbus.jlibmodbus.serial.SerialUtils;

import deviceconfig.DeviceConfig;
import net.lockoil.pimodbusmaster.model.ModbusReadRequest;
import net.lockoil.pimodbusmaster.model.ReadResponse;


@Service
public class ModbusRequest {
	
	public static List<String> courses = new ArrayList<>();
	
	
	public List<ReadResponse> read(ModbusReadRequest modbusReadRequest) {
		List<ReadResponse> responses = new ArrayList<>();
		
		int slave = modbusReadRequest.getSlave(); 
		int address = modbusReadRequest.getAddress(); 
		int count = modbusReadRequest.getCount();
		
		
		Modbus.setLogLevel(Modbus.LogLevel.LEVEL_DEBUG);
        
        ModbusMaster modbusMaster = null;
		try {
			modbusMaster = ModbusMasterFactory.createModbusMasterRTU(DeviceConfig.getStandartDevice());
			modbusMaster.connect();
			int[] registerValues = modbusMaster.readHoldingRegisters(slave, address, count);
               // print values
            for (int value : registerValues) {
            	address++;
            	responses.add(new ReadResponse(address, value));
             }
		} catch (SerialPortException | ModbusIOException e) {
			courses.add("Ошибка " + e.getClass().getSimpleName());
			e.printStackTrace();
		} catch (RuntimeException e) {
                throw e;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                	modbusMaster.disconnect();
                } catch (ModbusIOException e) {
                    e.printStackTrace();
                }
            }
		
		return responses;
	}
}
