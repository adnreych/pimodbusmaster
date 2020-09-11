package net.lockoil.pimodbusmaster.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.intelligt.modbus.jlibmodbus.Modbus;
import com.intelligt.modbus.jlibmodbus.exception.ModbusIOException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusNumberException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusProtocolException;
import com.intelligt.modbus.jlibmodbus.master.ModbusMaster;
import com.intelligt.modbus.jlibmodbus.master.ModbusMasterFactory;
import com.intelligt.modbus.jlibmodbus.serial.SerialPortException;
import net.lockoil.pimodbusmaster.csd.ATConnect;
import net.lockoil.pimodbusmaster.csd.CSDCommand;
import net.lockoil.pimodbusmaster.csd.CSDRequestPayloadAssembler;
import net.lockoil.pimodbusmaster.csd.CSDResponsePayloadParser;
import net.lockoil.pimodbusmaster.csd.Utils;
import net.lockoil.pimodbusmaster.deviceconfig.DeviceConfig;
import net.lockoil.pimodbusmaster.model.AtConnectionRequest;
import net.lockoil.pimodbusmaster.model.CardRegisterElement;
import net.lockoil.pimodbusmaster.model.ReadRequest;
import net.lockoil.pimodbusmaster.model.ReadResponse;
import net.lockoil.pimodbusmaster.model.WriteRequest;
import net.lockoil.pimodbusmaster.model.modbustypes.AbstractModbusType;
import net.lockoil.pimodbusmaster.util.ModbusTypeParser;


@Service
public class ModbusRequestService {
	
	private final Logger log = Logger.getLogger(this.getClass().getSimpleName());

	@Autowired
	private ModbusTypeParser modbusTypeParser;
	
	@Autowired
	private CSDRequestPayloadAssembler csdRequestPayloadAssembler;
	
	@Autowired
	private ATConnect atConnect;
	
	@Autowired
	private RegistersService registersService;
		
	public List<Object> read(ReadRequest modbusReadRequest) {
		AbstractModbusType abstractModbusType;
		List<ReadResponse> responses = new ArrayList<>();
		
		int slave = modbusReadRequest.getSlave(); 
		int address = modbusReadRequest.getAddress(); 
		int count = modbusReadRequest.getCount();
				
		Modbus.setLogLevel(Modbus.LogLevel.LEVEL_DEBUG);
        
        ModbusMaster modbusMaster = null;
		try {
			if (!modbusReadRequest.isCSD()) {
				modbusMaster = ModbusMasterFactory.createModbusMasterRTU(DeviceConfig.getByRPiConnection());
				modbusMaster.connect();

				int[] registerValues = modbusMaster.readHoldingRegisters(slave, address, count);
				
				int iterAddress = address;
	            for (int value : registerValues) {
	            	iterAddress++;
	            	log.info("addr: " + iterAddress + " val: " + value);
	            	responses.add(new ReadResponse(iterAddress, value));
	             }
			} else {
				CSDCommand csdCommand = new CSDCommand(csdRequestPayloadAssembler.readRequestPayloadAssemble(modbusReadRequest));
				System.out.println("CSDCommand:" + csdCommand.toString());
				byte[] data = atConnect.CSDReadRequest(modbusReadRequest.getAtConnectionRequest(), csdCommand.getCommand());
				byte[] commandId = Utils.getCSDCommand(slave, true);
				CSDResponsePayloadParser csdResponsePayloadParser =  new CSDResponsePayloadParser(address, count, commandId, data);
				
				int[] registerValues = csdResponsePayloadParser.parseReadResponse();
				
				int iterAddress = address;
	            for (int value : registerValues) {
	            	iterAddress++;
	            	log.info("addr: " + iterAddress + " val: " + value);
	            	responses.add(new ReadResponse(iterAddress, value));
	             }	            	        
			}
			
			if (modbusReadRequest.isReadGroups()) {
				Map<ReadRequest, List<ReadResponse>> splitReqResp = handleGroupRead(modbusReadRequest, responses);
				List<Object> result = new ArrayList<>();
				System.out.println("splitReqResp" + splitReqResp.toString());
				for(ReadRequest readRequest : splitReqResp.keySet()) {
					result.add(modbusTypeParser.parseRead(splitReqResp.get(readRequest), readRequest).readValue());
				}
				return result;
			} else {
				abstractModbusType = modbusTypeParser.parseRead(responses, modbusReadRequest);
	    		return Collections.singletonList(abstractModbusType.readValue());
			}
			
			                     
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
                	if(modbusMaster != null) {
                		modbusMaster.disconnect();
                	}
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
			if (!modbusWriteRequest.isCSD()) {
				modbusMaster = ModbusMasterFactory.createModbusMasterRTU(DeviceConfig.getByRPiConnection());
				modbusMaster.connect();
				modbusMaster.writeMultipleRegisters(slave, startAddress, values);
			} else {
				CSDCommand csdCommand = new CSDCommand(csdRequestPayloadAssembler.writeRequestPayloadAssemble(modbusWriteRequest));
				System.out.println("CSDCommand:" + csdCommand.toString());
				atConnect.CSDWriteRequest(modbusWriteRequest.getAtConnectionRequest(), csdCommand.getCommand());
			}
			return "OK";
		} catch (SerialPortException | ModbusIOException | ModbusProtocolException | ModbusNumberException | RuntimeException e) {
			log.info(e.getClass().getSimpleName());
			e.printStackTrace();
		}
		finally {
                try {
                	if (modbusMaster != null && modbusMaster.isConnected()) {
                		modbusMaster.disconnect();
                	}
                } catch (ModbusIOException e) {
                    e.printStackTrace();
                    log.info(e.getClass().getSimpleName());              
                }
        }
		return "ERROR";
	}	
	
	private Map<ReadRequest, List<ReadResponse>> handleGroupRead(ReadRequest modbusReadRequest, List<ReadResponse> responses) {
		Map<ReadRequest, List<ReadResponse>> splitReqResp = new LinkedHashMap<>();
		int countSumm = modbusReadRequest.getCount();
		System.out.println("countSumm" + countSumm);
		while (countSumm > 0) {
			List<ReadResponse> innerResponses = new ArrayList<>();
			int address =  modbusReadRequest.getAddress() + (modbusReadRequest.getCount() - countSumm);
			CardRegisterElement cardRegisterElement = registersService.getRegister(modbusReadRequest.getDeviceId(), address);
			int slave = modbusReadRequest.getSlave();
			Long deviceId = modbusReadRequest.getDeviceId();
			int count = cardRegisterElement.getCount();
			String type = cardRegisterElement.getType();
			boolean isCSD = modbusReadRequest.isCSD();
			AtConnectionRequest atConnectionRequest = isCSD ? modbusReadRequest.getAtConnectionRequest() : null;		
			ReadRequest innerReadRequest = new ReadRequest(slave, address, deviceId, count, type, isCSD, atConnectionRequest, false);
			System.out.println("innerReadRequest" + innerReadRequest.toString());
			switch (innerReadRequest.getType()) {
			
			case "Float":
			case "UnsignedInt32":
				innerResponses.add(responses.remove(0));
				innerResponses.add(responses.remove(0));
				break;
				
			case "Multiple":
			case "CommaFloat":
				List<ReadResponse> list = responses
						.stream()
						.limit(innerReadRequest.getCount())
						.collect(Collectors.toList());
				innerResponses.addAll(list);
				responses.removeAll(list);
				break;
				
			default:
				innerResponses.add(responses.remove(0));
				break;
			}
			System.out.println("innerResponses" + innerReadRequest.toString());
			splitReqResp.putIfAbsent(innerReadRequest, innerResponses);		
			
			countSumm = countSumm - count;	
		}
		
		return splitReqResp;	
	}
}
