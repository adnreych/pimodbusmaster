package net.lockoil.pimodbusmaster.csd;

import java.math.BigInteger;

/**
 * Утилиты для работы с типом byte
 */
public class Utils {
	
	/**
	 * Преобразовать массив примитивов в массив {@link Byte}
	 * @param Массив типа byte
	 * @return Массив типа {@link Byte}
	 */
	public static Byte[] toByteWrap (byte[] input) {
		Byte[] result = new Byte[input.length];
		for(int i=0; i < input.length; i++) {
			result[i] = input[i];
		}
		return result;
	}
	
	/**
	 * Преобразовать массив оберток {@link Byte} в массив примитивов
	 * @param Массив типа {@link Byte}
	 * @return Массив типа byte
	 */
	public static byte[] toBytePrimitive(Byte[] input) {
		byte[] result = new byte[input.length];
		for(int i=0; i < input.length; i++) {
			result[i] = input[i];
		}
		return result;
	}
	
	/**
	 * Подготовка числа для записи в контроллер
	 * @param Число, которое надо подготовить
	 * @return Массив типа Byte
	 */
	public static Byte[] prepareBytesToWrite (int value) {
	    BigInteger bigInt = BigInteger.valueOf(value); 
	    return toByteWrap(bigInt.toByteArray());		
	}
	
	/**
	 * Подготовка номера CSD-команды
	 * @param slave адрес устройства
	 * @param isRead тип операции - чтение или запись
	 * @return Массив типа byte
	 */
	public static byte[] getCSDCommand(int slave, boolean isRead) {
		if (isRead) {
			switch (slave) {
				case 1:
					return "3".getBytes();
				case 65:
					return "17".getBytes();
				default:
					return "7".getBytes();
			}
		} else {
				switch (slave) {
				case 1:
					return "6".getBytes();
				case 65:
					return "18".getBytes();
				default:
					return "8".getBytes();
			}
		}
	}
	
	
	
	

}
