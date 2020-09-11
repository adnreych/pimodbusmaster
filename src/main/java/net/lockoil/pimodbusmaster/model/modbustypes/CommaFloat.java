package net.lockoil.pimodbusmaster.model.modbustypes;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;

/**
 * Тип, где можно указать количество знаков после запятой
 */
@Setter
@Getter
public class CommaFloat implements AbstractModbusType<Double, Double> {

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
								.peek((e) -> System.out.println("e " + e))
								.flatMap((e) -> Arrays.asList(
														Integer.valueOf(String.format("%02d", e).substring(0, 1)), 
														Integer.valueOf(String.format("%02d", e).substring(1, 2))
													)
												.stream()
												)
								.collect(Collectors.toList());
		
		String integerPart = values
								.stream()
								.limit(values.size() - signsCount)
								.map(e -> e.toString())
								.reduce((e1, e2) -> e1 + e2)
								.get();
		
		String floatPart = values
									.stream()
									.skip(values.size() - signsCount)
									.map(e -> e.toString())
									.reduce((e1, e2) -> e1 + e2)
									.get();	
		
		return Double.parseDouble(integerPart.concat(".").concat(floatPart));
	}

	@Override
	public void writeValue(Double value) {

	}

}
