package net.lockoil.pimodbusmaster.model.modbustypes;

import java.util.HashMap;
import java.util.List;

import java.util.Map;

import com.google.common.base.Strings;

/*
 * Хранит несколько значений в виде бит. Количество бит в 
 * {@link BitTypeLegend} равно количеству возможных значений
 * 
 */
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
		String binaryString;
		
		Integer sum = byteTypeLegends
				.stream()
				.map(element -> element.getBitQuantity())
				.reduce(0, Integer::sum);
		String arg = "%" + sum + "s";
		
		if (value != 0) {
			binaryString = Integer.toBinaryString(value).replace(" ", "0");
		} else {
			binaryString = Strings.repeat("0", sum);
		}
		
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
