package net.lockoil.pimodbusmaster.csd;

import java.nio.ByteBuffer;

import com.google.common.collect.ObjectArrays;

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
	
	public static byte[] prepareBytesToWrite (int[] input) {
		Byte[] swap = {};
		for(int curr : input) {
			Byte[] bytes = toByteWrap(ByteBuffer.allocate(4).putInt(curr).array());
			swap = ObjectArrays.concat(swap, bytes , Byte.class);
		}			
		return toBytePrimitive(swap);		
	}
	
	
	public static Byte[] preparePayload(Byte[] CSDcommandNumber) {
		
		return CSDcommandNumber;
	}
	
	

}
