package net.lockoil.pimodbusmaster.model.modbustypes;


/**
 * Несколько идущих подряд регистров имеют одинаковое описание, за исключением их порядкового номера (например номера смарт-карт)
 */
public class MultipleTypeModbus implements AbstractModbusType<Integer, Integer> {
	
	/**
	 * Регистры связаны друг с другом
	 */
	private boolean isSeparated;
	
	/**
	 * Количество элементов в кортеже
	 */
	private int elementsCount;
	
	/**
	 * Количество регистров, которое занимает один элемент кортежа
	 */
	private int elementLength;

	@Override
	public Integer readValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void writeValue(Integer value) {
		// TODO Auto-generated method stub
		
	}
	

}
