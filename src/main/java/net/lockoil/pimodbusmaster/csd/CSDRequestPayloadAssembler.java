package net.lockoil.pimodbusmaster.csd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import com.google.common.collect.ObjectArrays;

import net.lockoil.pimodbusmaster.model.ReadRequest;
import net.lockoil.pimodbusmaster.model.WriteRequest;

/**
 * Класс для подготовки полезной нагрузки для {@link CSDCommand}}
 */
@Component
public class CSDRequestPayloadAssembler {
	
	/**
	 * Подготовка полезной нагрузки для команды чтения
	 * @param {@link ReadRequest} параметры запроса
	 * @return Полезная нагрузка для команды чтения
	 */
	public Byte[] readRequestPayloadAssemble(ReadRequest readRequest) {
		List<Byte> payloadList = new ArrayList<Byte>();
		Byte[] addressArr;
		
		switch (readRequest.getSlave()) {
		case 1:
			// чтение из ЦП
			payloadList.addAll(Arrays.asList(Utils.toByteWrap("3".getBytes())));
			addressArr = Utils.prepareBytesToWrite(readRequest.getAddress());
			if (addressArr.length == 1) {
				addressArr = ObjectArrays.concat(new Byte[]{0}, new Byte[]{addressArr[0]} , Byte.class);
			}
			payloadList.addAll(Arrays.asList(addressArr));
			payloadList.addAll(Arrays.asList(Utils.prepareBytesToWrite(readRequest.getCount())));
			return payloadList.toArray(new Byte[payloadList.size()]);
		case 65:
			// чтение из процессора связи
			payloadList.addAll(Arrays.asList(Utils.toByteWrap("17".getBytes())));
			addressArr = Utils.prepareBytesToWrite(readRequest.getAddress());
			if (addressArr.length == 1) {
				addressArr = ObjectArrays.concat(new Byte[]{0}, new Byte[]{addressArr[0]} , Byte.class);
			}
			payloadList.addAll(Arrays.asList(addressArr));
			payloadList.addAll(Arrays.asList(Utils.prepareBytesToWrite(readRequest.getCount())));
			return payloadList.toArray(new Byte[payloadList.size()]);
		default:
			// чтение удаленных устройств
			payloadList.addAll(Arrays.asList(Utils.toByteWrap("7".getBytes())));
			addressArr = Utils.prepareBytesToWrite(readRequest.getAddress());
			if (addressArr.length == 1) {
				addressArr = ObjectArrays.concat(new Byte[]{0}, new Byte[]{addressArr[0]} , Byte.class);
			}
			payloadList.addAll(Arrays.asList(addressArr));
			payloadList.addAll(Arrays.asList(Utils.prepareBytesToWrite(readRequest.getCount())));
			payloadList.addAll(Arrays.asList(Utils.prepareBytesToWrite(readRequest.getSlave())));
			return payloadList.toArray(new Byte[payloadList.size()]);	
		}
	}
	
	/**
	 * Подготовка полезной нагрузки для команды записи
	 * @param {@link WriteRequest} параметры запроса
	 * @return Полезная нагрузка для команды записи
	 */
	public Byte[] writeRequestPayloadAssemble(WriteRequest writeRequest) {
		List<Byte> payloadList = new ArrayList<Byte>();
		Byte[] addressArr;
		
		
		switch (writeRequest.getSlave()) {
		case 1:
			// запись в ЦП
			payloadList.addAll(Arrays.asList(Utils.toByteWrap("6".getBytes())));
			addressArr = Utils.prepareBytesToWrite(writeRequest.getAddress());
			if (addressArr.length == 1) {
				addressArr = ObjectArrays.concat(new Byte[]{0}, new Byte[]{addressArr[0]} , Byte.class);
			}
			payloadList.addAll(Arrays.asList(addressArr));
			payloadList.addAll(Arrays.asList(Utils.prepareBytesToWrite(writeRequest.getValues().length)));
			for(int value : writeRequest.getValues()) {
				Byte[] currValArr;
				currValArr = Utils.prepareBytesToWrite(value);
				if (currValArr.length == 1) {
					currValArr = ObjectArrays.concat(new Byte[]{0}, new Byte[]{currValArr[0]} , Byte.class);
				}
				payloadList.addAll(Arrays.asList(currValArr));
			}
			return payloadList.toArray(new Byte[payloadList.size()]);
		case 65:
			// запись в процессор связи
			payloadList.addAll(Arrays.asList(Utils.toByteWrap("18".getBytes())));
			addressArr = Utils.prepareBytesToWrite(writeRequest.getAddress());
			if (addressArr.length == 1) {
				addressArr = ObjectArrays.concat(new Byte[]{0}, new Byte[]{addressArr[0]} , Byte.class);
			}
			payloadList.addAll(Arrays.asList(addressArr));
			payloadList.addAll(Arrays.asList(Utils.prepareBytesToWrite(writeRequest.getValues().length)));
			for(int value : writeRequest.getValues()) {
				Byte[] currValArr;
				currValArr = Utils.prepareBytesToWrite(value);
				if (currValArr.length == 1) {
					currValArr = ObjectArrays.concat(new Byte[]{0}, new Byte[]{currValArr[0]} , Byte.class);
				}
				payloadList.addAll(Arrays.asList(currValArr));
			}
			return payloadList.toArray(new Byte[payloadList.size()]);
		default:
			// запись в удаленное устройство
			payloadList.addAll(Arrays.asList(Utils.toByteWrap("8".getBytes())));
			addressArr = Utils.prepareBytesToWrite(writeRequest.getAddress());
			if (addressArr.length == 1) {
				addressArr = ObjectArrays.concat(new Byte[]{0}, new Byte[]{addressArr[0]} , Byte.class);
			}
			payloadList.addAll(Arrays.asList(addressArr));
			payloadList.addAll(Arrays.asList(Utils.prepareBytesToWrite(writeRequest.getValues().length)));
			payloadList.addAll(Arrays.asList(Utils.prepareBytesToWrite(writeRequest.getSlave())));
			for(int value : writeRequest.getValues()) {
				Byte[] currValArr;
				currValArr = Utils.prepareBytesToWrite(value);
				if (currValArr.length == 1) {
					currValArr = ObjectArrays.concat(new Byte[]{0}, new Byte[]{currValArr[0]} , Byte.class);
				}
				payloadList.addAll(Arrays.asList(currValArr));
			}
			return payloadList.toArray(new Byte[payloadList.size()]);
		}	
	}
	
}
