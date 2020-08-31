package net.lockoil.pimodbusmaster.model.modbustypes;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.xml.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import net.lockoil.pimodbusmaster.util.ModbusTypeParser;

/**
 * Несколько идущих подряд регистров
 */
public class MultipleTypeModbus implements AbstractModbusType<Integer, List<Object>> {
	
	public MultipleTypeModbus(List<Integer> value, String single, String legend) {
		this.value = value;
		this.single = single;
		this.legend = legend;
		isASCII = true;
	}
	
	public MultipleTypeModbus(List<Integer> value) {
		this.value = value;
		isASCII = false;
	}
	
	private List<Integer> value;

	/**
	 * В регистрах хранится ASCII строка
	 */
	private boolean isASCII;	
	
	/**
	 * Тип элемента, если {@link #isASCII} null
	 */
	private String single;
		
	/**
	 * Не равен null если {@link #single} это Variable, Bit или Box
	 */
	private String legend;
	
	@Autowired
	private  ModbusTypeParser modbusTypeParser;

	@Override
	public List<Object> readValue() {
		if (isASCII) {
			switch (single) {
			case "UnsignedInt":
				return value
						.stream()
						.map(e -> new UnsignedInt(e))
						.collect(Collectors.toList())
						.stream()
						.map(e -> e.readValue())
						.collect(Collectors.toList());
			case "SignedInt":
				return value
						.stream()
						.map(e -> new SignedInt(e))
						.collect(Collectors.toList())
						.stream()
						.map(e -> e.readValue())
						.collect(Collectors.toList());
			case "Float":
				return IntStream
						.range(0, value.size())
						.filter(i -> i % 2 == 0)
						.mapToObj(i -> new FloatModbus(Pair.of(value.get(i), value.get(i + 1))))
						.collect(Collectors.toList())
						.stream()
						.map(e -> e.readValue())
						.collect(Collectors.toList());
			case "Variable":			
				try {
					ObjectMapper objectMapper = new ObjectMapper();
					List<VarTypeLegend> varTypeLegends;
					varTypeLegends = objectMapper.readValue(legend, new TypeReference<List<VarTypeLegend>>(){});
					return value
							.stream()
							.map(e -> new VarTypeModbus(varTypeLegends, e))
							.collect(Collectors.toList())
							.stream()
							.map(e -> e.readValue())
							.collect(Collectors.toList());
				} catch (JsonProcessingException e1) {
					e1.printStackTrace();
				}
				break;
			case "Bit":
				try {
					ObjectMapper objectMapper = new ObjectMapper();
					List<BitTypeLegend> bitTypeLegends;
					bitTypeLegends = objectMapper.readValue(legend, new TypeReference<List<BitTypeLegend>>(){});
					return value
							.stream()
							.map(e -> new BitTypeModbus(bitTypeLegends, e))
							.collect(Collectors.toList())
							.stream()
							.map(e -> e.readValue())
							.collect(Collectors.toList());
				} catch (JsonProcessingException e1) {
					e1.printStackTrace();
				}
				break;
			case "Box":
				try {
					ObjectMapper objectMapper = new ObjectMapper();
					JsonNode jsonNode;
					jsonNode = objectMapper.readTree(legend);
					JsonNode first = jsonNode.get("first");
					JsonNode second = jsonNode.get("second");
					return value
							.stream()
							.map(e -> Pair.of(
									Integer.parseInt(String.format("%04x", e).substring(0, 2), 16), 
									Integer.parseInt(String.format("%04x", e).substring(2, 4), 16))
									)
							.map(e -> new BoxTypeModbus(
									Pair.of(modbusTypeParser.parsePairElement(first, e.getFirst()), 
											modbusTypeParser.parsePairElement(second, e.getSecond()))))
							.collect(Collectors.toList())
							.stream()
							.map(e -> e.readValue())
							.collect(Collectors.toList());
				} catch (JsonProcessingException e1) {
					e1.printStackTrace();
				}
				break;	
			default:
				break;
			}
		} else {
			String result = value.stream().map(e -> Pair.of(
							Integer.parseInt(String.format("%04x", e).substring(0, 2), 16), 
							Integer.parseInt(String.format("%04x", e).substring(2, 4), 16)))
						  .map(e -> Character.toString((char) e.getFirst().intValue()) + Character.toString((char) e.getSecond().intValue()))
						  .collect(Collectors.toList())
						  .stream()
						  .reduce("", (subtotal, element) -> subtotal + element);
			return Collections.singletonList(result);
		}
		return null;
	}

	@Override
	public void writeValue(Integer value) {
		// TODO Auto-generated method stub
		
	}
	

}
