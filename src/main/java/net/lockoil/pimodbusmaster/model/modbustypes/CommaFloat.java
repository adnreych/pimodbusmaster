package net.lockoil.pimodbusmaster.model.modbustypes;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Тип, где можно указать количество знаков после запятой
 */
public class CommaFloat implements AbstractModbusType<Integer, Double> {

	List<Integer> value;
	
	/**
	 * Количество знаков после запятой
	 */
	Integer signsCount;
	
	

	public CommaFloat(List<Integer> value, Integer signsCount) {
		this.value = value;
		this.signsCount = signsCount;
	}

	@Override
	public Double readValue() {
		List<Integer> values = value
								.stream()
								.flatMap((e) -> Arrays.asList(
														Integer.valueOf(String.format("%04d", e).substring(0, 2)), 
														Integer.valueOf(String.format("%04d", e).substring(2, 4))
													)
												.stream()
												)
								.collect(Collectors.toList());
		
		String decimalPart = values
								.stream()
								.limit(values.size() - signsCount)
								.map(e -> e.toString())
								.reduce((e1, e2) -> e1 + e2)
								.get();
		
		String fractionalPart = values
									.stream()
									.skip(values.size() - signsCount)
									.map(e -> e.toString())
									.reduce((e1, e2) -> e1 + e2)
									.get();		
		
		return Double.parseDouble(decimalPart.concat(".").concat(fractionalPart));
	}

	@Override
	public void writeValue(Integer value) {
		

	}

}
