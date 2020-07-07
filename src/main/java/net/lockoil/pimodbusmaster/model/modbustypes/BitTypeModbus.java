package net.lockoil.pimodbusmaster.model.modbustypes;

import java.util.HashMap;
import java.util.List;

import java.util.Map;

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
		String binaryString = Integer.toBinaryString(value).replace(" ", "0");
		String byteString = String.format(arg, binaryString);

		for(BitTypeLegend bitTypeLegend : byteTypeLegends) {
			Integer startBit = bitTypeLegend.getStartBit();			
			Integer currentBitValue = Integer.parseInt(byteString.substring(startBit, startBit + bitTypeLegend.getBitQuantity()).trim(), 2);
			valuesMap.put(bitTypeLegend.getDescription(), bitTypeLegend.getPossibleValues().get(currentBitValue));
		}
		return valuesMap;
	}

	@Override
	public void writeValue(Integer value) {
		this.value = value;	
	}
	
	
	

}
