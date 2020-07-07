package net.lockoil.pimodbusmaster.model.modbustypes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class BitTypeModbus implements AbstractModbusType<Integer, Map<String, String>> {
	
	private Integer value;
	private List<BitTypeLegend> byteTypeLegends;
	
	public BitTypeModbus(List<BitTypeLegend> byteTypeLegends, Integer value) {
		this.byteTypeLegends = byteTypeLegends;
		this.value = value;
	}	
	
	@Override
	public Map<String, String> readValue() {
		Map<String, String> valuesMap = new HashMap<>();
		
		Integer sum = byteTypeLegends
				.stream()
				.map(element -> element.getBitQuantity())
				.reduce(0, Integer::sum);
		String arg = "%" + sum + "s";
		System.out.println("ARG" + arg);
		String binaryString = Integer.toBinaryString(value).replace(" ", "0");
		System.out.println("binaryString" + binaryString);
		String byteString = String.format(arg, binaryString);

		for(BitTypeLegend bitTypeLegend : byteTypeLegends) {
			Integer startBit = bitTypeLegend.getStartBit();			
			Integer currentBitValue = Integer.parseInt(byteString.substring(startBit, startBit + bitTypeLegend.getBitQuantity()).trim(), 2);
			System.out.println("startBit " + startBit + " currentBitValue " + currentBitValue + " possVal " + bitTypeLegend.getPossibleValues().toString() + " desc " + bitTypeLegend.getDescription());
			valuesMap.put(bitTypeLegend.getDescription(), bitTypeLegend.getPossibleValues().get(currentBitValue));
		}
		return valuesMap;
	}

	@Override
	public void writeValue(Integer value) {
		this.value = value;	
	}
	
	
	

}
