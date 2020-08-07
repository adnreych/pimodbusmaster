package net.lockoil.pimodbusmaster.csd;

import java.math.BigInteger;

public class Utils {
	
	
	public static Byte[] toByteWrap (byte[] input) {
		Byte[] result = new Byte[input.length];
		for(int i=0; i < input.length; i++) {
			result[i] = input[i];
		}
		return result;
	}
	
	public static byte[] toBytePrimitive (Byte[] input) {
		byte[] result = new byte[input.length];
		for(int i=0; i < input.length; i++) {
			result[i] = input[i];
		}
		return result;
	}
	
	public static Byte[] prepareBytesToWrite (int i) {
	    BigInteger bigInt = BigInteger.valueOf(i);     
	    return toByteWrap(bigInt.toByteArray());		
	}
	
	
	
	

}
