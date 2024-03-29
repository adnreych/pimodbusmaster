package net.lockoil.pimodbusmaster.util;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.lockoil.pimodbusmaster.exceptions.IllegalModbusTypeException;
import net.lockoil.pimodbusmaster.model.CardRegisterElement;
import net.lockoil.pimodbusmaster.model.ReadRequest;
import net.lockoil.pimodbusmaster.model.ReadResponse;
import net.lockoil.pimodbusmaster.model.modbustypes.AbstractModbusType;
import net.lockoil.pimodbusmaster.model.modbustypes.BitTypeLegend;
import net.lockoil.pimodbusmaster.model.modbustypes.BitTypeModbus;
import net.lockoil.pimodbusmaster.model.modbustypes.BoxTypeModbus;
import net.lockoil.pimodbusmaster.model.modbustypes.CommaFloat;
import net.lockoil.pimodbusmaster.model.modbustypes.FloatModbus;
import net.lockoil.pimodbusmaster.model.modbustypes.MultipleTypeModbus;
import net.lockoil.pimodbusmaster.model.modbustypes.SignedInt;
import net.lockoil.pimodbusmaster.model.modbustypes.UnsignedInt;
import net.lockoil.pimodbusmaster.model.modbustypes.UnsignedInt32;
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
	
	/**
	 * Возвращает конкретную реализацию AbstractModbusType.
	 * Добавляя новый тип сюда, его же нужно добавить в метод 
	 * {@link net.lockoil.pimodbusmaster.service.ModbusRequestService#handleGroupRead(ReadRequest, List<ReadResponse>) handleGroupRead}
	 * если это необходимо
	 */
	public AbstractModbusType parseRead(List<ReadResponse> response, ReadRequest request) throws IllegalModbusTypeException {
		
		switch (request.getType()) {
		case "UnsignedInt":
			return new UnsignedInt(response.get(0).getValue());
			
		case "UnsignedInt32":
			return new UnsignedInt32(Pair.of(response.get(0).getValue(), response.get(1).getValue()));
			
		case "SignedInt":
			return new SignedInt(response.get(0).getValue());
			
		case "Float":
			return new FloatModbus(Pair.of(response.get(0).getValue(), response.get(1).getValue()));
			
		case "CommaFloat":
			return getCommaFloatType(response, request);
	
		case "Bit":
			return getBitType(response.get(0), request);
			
		case "Variable":
			return getVarType(response.get(0), request);
			
		case "Box":
			return getBoxType(response.get(0), request);
			
		case "Multiple":
			return getMultipleType(response, request);

		default:
			throw new IllegalModbusTypeException();
		}					
	}
	
	private CommaFloat getCommaFloatType(List<ReadResponse> readResponse, ReadRequest readRequest) {
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode;
		CardRegisterElement cardRegisterElement = registersService.getRegister(readRequest.getDeviceId(), readRequest.getAddress());
		List<Integer> values = readResponse
				.stream()
				.map(e -> e.getValue())
				.collect(Collectors.toList());
		try {
			jsonNode = objectMapper.readTree(cardRegisterElement.getLegends());
			String signsCountStr = jsonNode.has("signsCount") ? jsonNode.get("signsCount").asText() : "2";
			Integer signsCount = Integer.valueOf(signsCountStr);
			
			return new CommaFloat(values, signsCount);
			
		} catch (JsonMappingException e) {
			log.error(e.getClass().getSimpleName() + " - " + Arrays.asList(e.getStackTrace()).toString()); 
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			log.error(e.getClass().getSimpleName() + " - " + Arrays.asList(e.getStackTrace()).toString()); 
			e.printStackTrace();
		}
		return null;
	}
	
	private MultipleTypeModbus getMultipleType(List<ReadResponse> readResponse, ReadRequest readRequest) {
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode;
		CardRegisterElement cardRegisterElement = registersService.getRegister(readRequest.getDeviceId(), readRequest.getAddress());
		List<Integer> values = readResponse
									.stream()
									.map(e -> e.getValue())
									.collect(Collectors.toList());		
		try {
			jsonNode = objectMapper.readTree(cardRegisterElement.getLegends());
			String single = jsonNode.has("single") ? jsonNode.get("single").asText() : null;
			String legend = jsonNode.has("legend") ? jsonNode.get("legend").asText() : "";
			
			if (single == null) {
				return new MultipleTypeModbus(values);
			} else {
				return new MultipleTypeModbus(values, single, legend);
			}
			
		} catch (JsonMappingException e) {
			log.error(e.getClass().getSimpleName() + " - " + Arrays.asList(e.getStackTrace()).toString()); 
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			log.error(e.getClass().getSimpleName() + " - " + Arrays.asList(e.getStackTrace()).toString()); 
			e.printStackTrace();
		}
		return null;
		
	}
	
	private BoxTypeModbus getBoxType(ReadResponse readResponse, ReadRequest readRequest) {	
		CardRegisterElement cardRegisterElement = registersService.getRegister(readRequest.getDeviceId(), readRequest.getAddress());
		Pair<AbstractModbusType, AbstractModbusType> boxTypeLegends;
		
		boxTypeLegends = parseBoxPair(cardRegisterElement.getLegends(), readResponse);	
		
		return new BoxTypeModbus(boxTypeLegends);	
	}
	
	private Pair<AbstractModbusType, AbstractModbusType> parseBoxPair(String legend, ReadResponse readResponse) {
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode;
		
		try {		
			Integer value = readResponse.getValue();
			String hexValStr = String.format("%04x", value);
			Integer firstVal = Integer.parseInt(hexValStr.substring(0, 2), 16);
			Integer secondVal = Integer.parseInt(hexValStr.substring(2, 4), 16);
			
			jsonNode = objectMapper.readTree(legend);
			JsonNode first = jsonNode.get("first");
			JsonNode second = jsonNode.get("second");
			
			AbstractModbusType firstInBox = parsePairElement(first, firstVal);
			AbstractModbusType secondInBox = parsePairElement(second, secondVal);
			
			return Pair.of(firstInBox, secondInBox);
		} catch (JsonMappingException e) {
			log.error(e.getClass().getSimpleName() + " - " + Arrays.asList(e.getStackTrace()).toString()); 
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			log.error(e.getClass().getSimpleName() + " - " + Arrays.asList(e.getStackTrace()).toString()); 
			e.printStackTrace();
		}		
		return null;		
	}
	
	public AbstractModbusType parsePairElement(JsonNode pairElement, int value) 
			 {
		ObjectMapper objectMapper = new ObjectMapper();
		switch (pairElement.get("type").asText()) {
		case "UnsignedInt":
			return new UnsignedInt(value);
		case "SignedInt":
			return new SignedInt(value);
		case "Bit":
			List<BitTypeLegend> bitTypeLegends;
			try {
				bitTypeLegends = objectMapper.readValue(pairElement.get("content").toPrettyString(), new TypeReference<List<BitTypeLegend>>(){});
				return new BitTypeModbus(bitTypeLegends, value);
			} catch (JsonMappingException e) {
				log.error(e.getClass().getSimpleName() + " - " + Arrays.asList(e.getStackTrace()).toString()); 
				e.printStackTrace();
			} catch (JsonProcessingException e) {
				log.error(e.getClass().getSimpleName() + " - " + Arrays.asList(e.getStackTrace()).toString()); 
				e.printStackTrace();
			}
			return null;
		case "Variable":
			List<VarTypeLegend> varTypeLegends;
			try {
				varTypeLegends = objectMapper.readValue(pairElement.get("content").toPrettyString(), new TypeReference<List<VarTypeLegend>>(){});
			} catch (JsonMappingException e) {
				log.error(e.getClass().getSimpleName() + " - " + Arrays.asList(e.getStackTrace()).toString()); 
				e.printStackTrace();
			} catch (JsonProcessingException e) {
				log.error(e.getClass().getSimpleName() + " - " + Arrays.asList(e.getStackTrace()).toString()); 
				e.printStackTrace();
			}
			return null;
		default:
			return null;
		}
	}
	
	private BitTypeModbus getBitType(ReadResponse readResponse, ReadRequest readRequest) {
		CardRegisterElement cardRegisterElement = registersService.getRegister(readRequest.getDeviceId(), readRequest.getAddress());
		String legendString = cardRegisterElement.getLegends();
		ObjectMapper objectMapper = new ObjectMapper();
	
		List<BitTypeLegend> bitTypeLegends;
		try {
			bitTypeLegends = objectMapper.readValue(legendString, new TypeReference<List<BitTypeLegend>>(){});		
			
			return new BitTypeModbus(bitTypeLegends, readResponse.getValue());
		} catch (JsonProcessingException e) {
			log.error(e.getClass().getSimpleName() + " - " + Arrays.asList(e.getStackTrace()).toString()); 
			e.printStackTrace();
		}
		return null;
	}
	
	private VarTypeModbus getVarType(ReadResponse readResponse, ReadRequest readRequest) {
		CardRegisterElement cardRegisterElement = registersService.getRegister(readRequest.getDeviceId(), readRequest.getAddress());
		String legendString = cardRegisterElement.getLegends();
		ObjectMapper objectMapper = new ObjectMapper();
	
		List<VarTypeLegend> varTypeLegends;
		try {
			varTypeLegends = objectMapper.readValue(legendString, new TypeReference<List<VarTypeLegend>>(){});
			
			return new VarTypeModbus(varTypeLegends, readResponse.getValue());
		} catch (JsonProcessingException e) {
			log.error(e.getClass().getSimpleName() + " - " + Arrays.asList(e.getStackTrace()).toString()); 
			e.printStackTrace();
		}
		return null;
	}
	

}
