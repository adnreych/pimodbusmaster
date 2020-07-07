package net.lockoil.pimodbusmaster.model.modbustypes;

import java.math.BigInteger;
import java.util.Locale;

import org.springframework.data.util.Pair;

public class FloatModbus implements AbstractModbusType<Double, Double> {
	
	private Pair<Integer, Integer> value;
	
	public FloatModbus(Pair<Integer, Integer> value) {
		this.value = value;
	}

	@Override
	public Double readValue() {
		return getFloat32(value);
	}

	@Override
	public void writeValue(Double value) {
		this.value = floatArrToWrite(value);	
	}
	
	private double getFloat32(Pair<Integer, Integer> value) {
        String s = "" + String.format(Locale.getDefault(),"%016d", Integer.parseInt(Integer.toBinaryString(value.getFirst()))) +
        		String.format(Locale.getDefault(),"%016d", Integer.parseInt(Integer.toBinaryString(value.getSecond())));
        int intBits = new BigInteger(s, 2).intValue();
        Float myFloat = Float.intBitsToFloat(intBits);
        return myFloat.doubleValue();
    }
	
	private Pair<Integer, Integer> floatArrToWrite(Double value) {
        int intBits = Float.floatToIntBits(value.floatValue());
        char[] chArr = Integer.toHexString(intBits).toCharArray();
        System.out.print(Integer.toHexString(intBits));
        Pair<Integer, Integer> pair;
        if (value != 0.0) {
        	pair = Pair.of(Integer.parseInt(Character.toString(chArr[4]) + chArr[5] + chArr[6] + chArr[7], 16),
        					Integer.parseInt(Character.toString(chArr[0]) + chArr[1] + chArr[2] + chArr[3], 16));
        } else {
        	pair = Pair.of(0,0);
        }
        return pair;
    }
	
	public Pair<Integer, Integer> getValue() {
		return value;
	}

}
