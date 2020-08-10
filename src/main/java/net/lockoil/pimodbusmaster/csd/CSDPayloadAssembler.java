package net.lockoil.pimodbusmaster.csd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import com.google.common.collect.ObjectArrays;

import net.lockoil.pimodbusmaster.model.ReadRequest;
import net.lockoil.pimodbusmaster.model.WriteRequest;

@Component
public class CSDPayloadAssembler {
	
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
				payloadList.addAll(Arrays.asList(Utils.prepareBytesToWrite(value)));
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
				payloadList.addAll(Arrays.asList(Utils.prepareBytesToWrite(value)));
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
				payloadList.addAll(Arrays.asList(Utils.prepareBytesToWrite(value)));
			}
			return payloadList.toArray(new Byte[payloadList.size()]);
		}	
	}
	
}
