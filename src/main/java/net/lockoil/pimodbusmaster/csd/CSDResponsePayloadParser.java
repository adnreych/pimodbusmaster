package net.lockoil.pimodbusmaster.csd;

import java.util.Arrays;
import java.util.List;

import net.lockoil.pimodbusmaster.exceptions.CSDException;


public class CSDResponsePayloadParser {
	
	
	
	public CSDResponsePayloadParser(int startAddress, int count, byte[] cSDcommandNumber, String response) {
		this.startAddress = startAddress;
		this.count = count;
		CSDcommandNumber = cSDcommandNumber;
		this.response = response;
	}

	private int startAddress;
	private int count;
	private byte[] CSDcommandNumber;
	private String response;
	
	
	public int[] parseReadResponse() {
		try {
			parseError();
		} catch (CSDException e) {
			e.printStackTrace();
		}
		int[] result = new int[count];
		List<String> res = Arrays.asList(response.split(" "));
		System.out.println("RES ARR" + res.toString());
		res = res.subList(3 + CSDcommandNumber.length, res.size() - 4);  // remove "CSD" in begin and "{CRC}ENDD" in end
		for(int i = 0; i < res.size(); i = i + 2) {
			String currString = res.get(i) + res.get(i+1);
			result[i/2] = Integer.parseInt(currString, 16);
		}
		return result;
	}

	private String parseError() throws CSDException {
		if (response.length() >= 14 && response.substring(0, 14).equals("45 52 52 4F 52")) { // 45 52 52 4F 52 == ERROR
			String errorCode = response.substring(15, 17);
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

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	@Override
	public String toString() {
		return "CSDResponsePayloadParser [startAddress=" + startAddress + ", count=" + count + ", CSDcommandNumber="
				+ Arrays.toString(CSDcommandNumber) + ", response=" + response + "]";
	}
	
	
	
	
	

	
	
	

}
