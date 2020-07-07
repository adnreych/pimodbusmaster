package net.lockoil.pimodbusmaster.model.modbustypes;

public class UnsignedInt implements AbstractModbusType<Integer, Integer> {
	
	private Integer value;
	
	public UnsignedInt(Integer value) {
		this.value = value;
	}
		
	@Override
	public Integer readValue() {
		return value & 0xFFFF;
	}


	@Override
	public void writeValue(Integer value) {
		this.value = value;		
	}	
	
	public Integer getValue() {
		return value;
	}
	

}
