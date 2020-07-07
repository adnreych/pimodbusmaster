package net.lockoil.pimodbusmaster.model.modbustypes;

public class SignedInt implements AbstractModbusType<Integer, Integer> {
	
	private Integer value;
	
	public SignedInt(Integer value) {
		this.value = value;
	}

	@Override
	public Integer readValue() {
		return value;
	}

	@Override
	public void writeValue(Integer value) {
		this.value = value;
	}
	
	public Integer getValue() {
		return value;
	}
	

}
