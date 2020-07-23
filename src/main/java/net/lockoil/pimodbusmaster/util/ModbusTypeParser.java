package net.lockoil.pimodbusmaster.util;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.lockoil.pimodbusmaster.exceptions.IllegalModbusTypeException;
import net.lockoil.pimodbusmaster.model.CardRegisterElement;
import net.lockoil.pimodbusmaster.model.ReadRequest;
import net.lockoil.pimodbusmaster.model.ReadResponse;
import net.lockoil.pimodbusmaster.model.modbustypes.AbstractModbusType;
import net.lockoil.pimodbusmaster.model.modbustypes.BitTypeLegend;
import net.lockoil.pimodbusmaster.model.modbustypes.BitTypeModbus;
import net.lockoil.pimodbusmaster.model.modbustypes.FloatModbus;
import net.lockoil.pimodbusmaster.model.modbustypes.SignedInt;
import net.lockoil.pimodbusmaster.model.modbustypes.UnsignedInt;
import net.lockoil.pimodbusmaster.model.modbustypes.VarTypeLegend;
import net.lockoil.pimodbusmaster.model.modbustypes.VarTypeModbus;
import net.lockoil.pimodbusmaster.service.RegistersService;

@Component
public class ModbusTypeParser {
	
	private final Logger log = Logger.getLogger(this.getClass().getSimpleName());
	
	private RegistersService registersService;
	
	
	@Autowired
	public ModbusTypeParser(RegistersService registersService) {
		this.registersService = registersService;
	}

	public AbstractModbusType parseRead(List<ReadResponse> response, ReadRequest request) throws IllegalModbusTypeException {
		
		switch (request.getType()) {
		case "UnsignedInt":
			return new UnsignedInt(response.get(0).getValue());
			
		case "SignedInt":
			return new SignedInt(response.get(0).getValue());
			
		case "Float":
			return new FloatModbus(Pair.of(response.get(0).getValue(), response.get(1).getValue()));
	
		case "Bit":
			return getBitType(response.get(0), request);
			
		case "Variable":
			return getVarType(response.get(0), request);

		default:
			throw new IllegalModbusTypeException();
		}					
	}
	
	@SuppressWarnings("unchecked")
	private BitTypeModbus getBitType(ReadResponse readResponse, ReadRequest readRequest) {
		CardRegisterElement cardRegisterElement = registersService.getRegister(readRequest.getSlave(), readRequest.getAddress());
		String legendString = cardRegisterElement.getLegends();
		ObjectMapper objectMapper = new ObjectMapper();
	
		List<BitTypeLegend> bitTypeLegends;
		try {
			bitTypeLegends = objectMapper.readValue(legendString, new TypeReference<List<BitTypeLegend>>(){});
		
			return new BitTypeModbus(bitTypeLegends, readResponse.getValue());
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private VarTypeModbus getVarType(ReadResponse readResponse, ReadRequest readRequest) {
		CardRegisterElement cardRegisterElement = registersService.getRegister(readRequest.getSlave(), readRequest.getAddress());
		String legendString = cardRegisterElement.getLegends();
		ObjectMapper objectMapper = new ObjectMapper();
	
		List<VarTypeLegend> varTypeLegends;
		try {
			varTypeLegends = objectMapper.readValue(legendString, new TypeReference<List<VarTypeLegend>>(){});
			
			return new VarTypeModbus(varTypeLegends, readResponse.getValue());
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private BitTypeLegend tryParseBitTypeModbus(String s) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.readValue(s, BitTypeLegend.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private VarTypeLegend tryParseVarTypeModbus(String s) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.readValue(s, VarTypeLegend.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}

}
