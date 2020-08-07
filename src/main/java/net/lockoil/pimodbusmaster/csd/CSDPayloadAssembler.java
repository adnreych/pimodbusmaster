package net.lockoil.pimodbusmaster.csd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import net.lockoil.pimodbusmaster.model.ReadRequest;
import net.lockoil.pimodbusmaster.model.WriteRequest;

@Component
public class CSDPayloadAssembler {
	
	public Byte[] readRequestPayloadAssemble(ReadRequest readRequest) {
		List<Byte> payloadList = new ArrayList<Byte>();
		
		switch (readRequest.getSlave()) {
		case 1:
			// чтение из ЦП
			payloadList.addAll(Arrays.asList(Utils.toByteWrap("3".getBytes())));
			payloadList.addAll(Arrays.asList(Utils.prepareBytesToWrite(readRequest.getAddress())));
			payloadList.addAll(Arrays.asList(Utils.prepareBytesToWrite(readRequest.getCount())));
			return payloadList.toArray(new Byte[payloadList.size()]);
		case 65:
			// чтение из процессора связи
			payloadList.addAll(Arrays.asList(Utils.toByteWrap("17".getBytes())));
			payloadList.addAll(Arrays.asList(Utils.prepareBytesToWrite(readRequest.getAddress())));
			payloadList.addAll(Arrays.asList(Utils.prepareBytesToWrite(readRequest.getCount())));
			return payloadList.toArray(new Byte[payloadList.size()]);
		default:
			// чтение удаленных устройств
			payloadList.addAll(Arrays.asList(Utils.toByteWrap("7".getBytes())));
			payloadList.addAll(Arrays.asList(Utils.prepareBytesToWrite(readRequest.getAddress())));
			payloadList.addAll(Arrays.asList(Utils.prepareBytesToWrite(readRequest.getCount())));
			payloadList.addAll(Arrays.asList(Utils.prepareBytesToWrite(readRequest.getSlave())));
			return payloadList.toArray(new Byte[payloadList.size()]);	
		}
	}
	
	public Byte[] writeRequestPayloadAssemble(WriteRequest writeRequest) {
		List<Byte> payloadList = new ArrayList<Byte>();
		
		switch (writeRequest.getSlave()) {
		case 1:
			// запись в ЦП
			payloadList.addAll(Arrays.asList(Utils.toByteWrap("6".getBytes())));
			payloadList.addAll(Arrays.asList(Utils.prepareBytesToWrite(writeRequest.getAddress())));
			payloadList.addAll(Arrays.asList(Utils.prepareBytesToWrite(writeRequest.getValues().length)));
			for(int value : writeRequest.getValues()) {
				payloadList.addAll(Arrays.asList(Utils.prepareBytesToWrite(value)));
			}
			return payloadList.toArray(new Byte[payloadList.size()]);
		case 65:
			// запись в процессор связи
			payloadList.addAll(Arrays.asList(Utils.toByteWrap("18".getBytes())));
			payloadList.addAll(Arrays.asList(Utils.prepareBytesToWrite(writeRequest.getAddress())));
			payloadList.addAll(Arrays.asList(Utils.prepareBytesToWrite(writeRequest.getValues().length)));
			for(int value : writeRequest.getValues()) {
				payloadList.addAll(Arrays.asList(Utils.prepareBytesToWrite(value)));
			}
			return payloadList.toArray(new Byte[payloadList.size()]);
		default:
			// запись в удаленное устройство
			payloadList.addAll(Arrays.asList(Utils.toByteWrap("8".getBytes())));
			payloadList.addAll(Arrays.asList(Utils.prepareBytesToWrite(writeRequest.getAddress())));
			payloadList.addAll(Arrays.asList(Utils.prepareBytesToWrite(writeRequest.getValues().length)));
			payloadList.addAll(Arrays.asList(Utils.prepareBytesToWrite(writeRequest.getSlave())));
			for(int value : writeRequest.getValues()) {
				payloadList.addAll(Arrays.asList(Utils.prepareBytesToWrite(value)));
			}
			return payloadList.toArray(new Byte[payloadList.size()]);
		}	
	}
	
}
