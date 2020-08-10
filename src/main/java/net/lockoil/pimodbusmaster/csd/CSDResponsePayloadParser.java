package net.lockoil.pimodbusmaster.csd;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

import net.lockoil.pimodbusmaster.exceptions.CSDException;


public class CSDResponsePayloadParser {
	
	
	
	public CSDResponsePayloadParser(int startAddress, int count, byte[] cSDcommandNumber, byte[] response) {
		this.startAddress = startAddress;
		this.count = count;
		CSDcommandNumber = cSDcommandNumber;
		this.response = response;
	}

	private int startAddress;
	private int count;
	private byte[] CSDcommandNumber;
	private byte[] response;
	
	
	public int[] parseReadResponse() {
		try {
			parseError();
		} catch (CSDException e) {
			e.printStackTrace();
		}
		int[] result = new int[count];
		Byte[] arrayResponse = Utils.toByteWrap(response);
		List<Byte> list = Arrays.asList(arrayResponse);
		
		list = list.subList(4, 4 + count * 2);   // удаляем заголовок и окончание
		
		System.out.println("RESPLIST" + list.toString());

		for(int i = 0, offset = 4; i < count; i++) {
			byte[] arr1 = {response[offset], response[offset + 1], response[offset + 2], response[offset + 3] };
			byte[] arr2 = {response[offset + 4], response[offset + 5], response[offset + 6], response[offset + 7] };
			int curr1 = ByteBuffer.wrap(arr1).getInt();
			int curr2 = ByteBuffer.wrap(arr2).getInt();
			System.out.println("CURR" + curr1 + " CURR2 " + curr2);
			result[i] = Integer.parseInt("" + curr1 + curr2);
			offset = offset + 8;
		}
		return result;
	}

	private String parseError() throws CSDException {
		
		if (response.length == 0) throw new CSDException("Пустой ответ на запрос");
		
		String result = "";
		for (int i=0; i<5; i++) {
			result = result + response[i];
		}
		
		if (response.length >= 10 && result.equals("4552524F52")) { // 45 52 52 4F 52 == ERROR
			String errorCode = response[5] + "";
			switch (errorCode) {
			case "30":
				throw new CSDException("Неизвестная команда");
			case "31":
				throw new CSDException("Неверный формат команды");
			case "32":
				throw new CSDException("Неверная контрольная сумма");
			case "35":
				throw new CSDException("Ошибка записи flash памяти");
			case "38":
				throw new CSDException("Нет ответа от удаленного устройства");
			default:
				throw new CSDException("Неизвестная ошибка CSD");
			}
		}
		return "OK";
	}

	public int getStartAddress() {
		return startAddress;
	}

	public void setStartAddress(int startAddress) {
		this.startAddress = startAddress;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public byte[] getCSDcommandNumber() {
		return CSDcommandNumber;
	}

	public void setCSDcommandNumber(byte[] cSDcommandNumber) {
		CSDcommandNumber = cSDcommandNumber;
	}

	public byte[] getResponse() {
		return response;
	}

	public void setResponse(byte[] response) {
		this.response = response;
	}

	@Override
	public String toString() {
		return "CSDResponsePayloadParser [startAddress=" + startAddress + ", count=" + count + ", CSDcommandNumber="
				+ Arrays.toString(CSDcommandNumber) + ", response=" + response + "]";
	}
	
	
	
	
	

	
	
	

}
