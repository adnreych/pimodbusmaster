package net.lockoil.pimodbusmaster.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.intelligt.modbus.jlibmodbus.Modbus;
import com.intelligt.modbus.jlibmodbus.exception.ModbusIOException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusNumberException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusProtocolException;
import com.intelligt.modbus.jlibmodbus.master.ModbusMaster;
import com.intelligt.modbus.jlibmodbus.master.ModbusMasterFactory;
import com.intelligt.modbus.jlibmodbus.serial.SerialParameters;
import com.intelligt.modbus.jlibmodbus.serial.SerialPort;
import com.intelligt.modbus.jlibmodbus.serial.SerialPortException;
import com.intelligt.modbus.jlibmodbus.serial.SerialPortFactoryJSSC;
import com.intelligt.modbus.jlibmodbus.serial.SerialUtils;

import net.lockoil.pimodbusmaster.deviceconfig.DeviceConfig;
import net.lockoil.pimodbusmaster.model.ReadRequest;
import net.lockoil.pimodbusmaster.model.ReadResponse;
import net.lockoil.pimodbusmaster.model.WriteRequest;
import net.lockoil.pimodbusmaster.model.modbustypes.AbstractModbusType;
import net.lockoil.pimodbusmaster.util.ModbusTypeParser;


@Service
public class ModbusRequestService {
	
	private final Logger log = Logger.getLogger(this.getClass().getSimpleName());
	
	@Autowired
	ModbusTypeParser modbusTypeParser;
	
	
	public Object read(ReadRequest modbusReadRequest) {
		AbstractModbusType abstractModbusType;
		List<ReadResponse> responses = new ArrayList<>();
		
		int slave = modbusReadRequest.getSlave(); 
		int address = modbusReadRequest.getAddress(); 
		int count = modbusReadRequest.getCount();
		
		
		Modbus.setLogLevel(Modbus.LogLevel.LEVEL_DEBUG);
        
        ModbusMaster modbusMaster = null;
		try {
			modbusMaster = ModbusMasterFactory.createModbusMasterRTU(DeviceConfig.getByRPiConnection());
			modbusMaster.connect();
			
			int[] registerValues = modbusMaster.readHoldingRegisters(slave, address, count);
			
            for (int value : registerValues) {
            	address++;
            	log.info("addr: " + address + " val: " + value);
            	responses.add(new ReadResponse(address, value));
             }
            
            abstractModbusType = modbusTypeParser.parseRead(responses, modbusReadRequest);
    		return abstractModbusType.readValue();
            
            
		} catch (SerialPortException | ModbusIOException e) {
			log.info(e.getClass().getSimpleName());
			e.printStackTrace();
		} catch (RuntimeException e) {
                throw e;
            } catch (Exception e) {
                e.printStackTrace();
                log.info(e.getClass().getSimpleName());
            } finally {
                try {
                	modbusMaster.disconnect();
                } catch (ModbusIOException e) {
                    e.printStackTrace();
                    log.info(e.getClass().getSimpleName());
                }
            }
		
		return null;
	}
	
	public String write(WriteRequest modbusWriteRequest) {

		int slave = modbusWriteRequest.getSlave(); 
		int startAddress = modbusWriteRequest.getAddress(); 
		int[] values = modbusWriteRequest.getValues();
		
		
		Modbus.setLogLevel(Modbus.LogLevel.LEVEL_DEBUG);
        
        ModbusMaster modbusMaster = null;
		try {
			modbusMaster = ModbusMasterFactory.createModbusMasterRTU(DeviceConfig.getByRPiConnection());
			modbusMaster.connect();
			modbusMaster.writeMultipleRegisters(slave, startAddress, values);
			return "OK";
		} catch (SerialPortException | ModbusIOException | ModbusProtocolException | ModbusNumberException | RuntimeException e) {
			log.info(e.getClass().getSimpleName());
			e.printStackTrace();
		}
		finally {
                try {
                	if (modbusMaster.isConnected()) modbusMaster.disconnect();  	
                	return "ERROR";
                } catch (ModbusIOException e) {
                    e.printStackTrace();
                    log.info(e.getClass().getSimpleName());              
                }
        }
		return "ERROR";
	}
	
}
