package net.lockoil.pimodbusmaster.model.modbustypes;

import org.springframework.data.util.Pair;

/**
 * Беззнаковое целое, занимает 2 регистра
 */
public class UnsignedInt32 implements AbstractModbusType<Integer, Integer> {
	
	private Pair<Integer, Integer> value;
	
	public UnsignedInt32(Pair<Integer, Integer> value) {
		this.value = value;
	}

	@Override
	public Integer readValue() {		
		return Integer.valueOf(String.format("%04d", value.getFirst()).concat(String.format("%04d", value.getSecond())));
	}

	@Override
	public void writeValue(Integer value) {
		String strVal = String.format("%08d", value);
		this.value = Pair.of(Integer.valueOf(strVal.substring(0, 4)), Integer.valueOf(strVal.substring(4, 8)));
	}

}
