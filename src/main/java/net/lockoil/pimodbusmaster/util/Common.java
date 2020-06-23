package net.lockoil.pimodbusmaster.util;

import net.lockoil.pimodbusmaster.model.CardRegisterElement;
import net.lockoil.pimodbusmaster.model.LoadRegistersResource;

public class Common {
	public static final String HSTORE_TYPE = "net.lockoil.pimodbusmaster.util.HStoreType";
	
	public static CardRegisterElement parseRegisterElement(LoadRegistersResource loadRegistersResource) {
		
		Long deviceId = loadRegistersResource.getDeviceId();
		String name = loadRegistersResource.getName();
		Integer address = loadRegistersResource.getAddress();
		Integer count = loadRegistersResource.getCount();
		Boolean isWrite = loadRegistersResource.getIsWrite();
		Boolean isRead = loadRegistersResource.getIsRead();
		String type = loadRegistersResource.getType();
		String suffix = loadRegistersResource.getSuffix() != null ? loadRegistersResource.getSuffix() : null;
		Long min = loadRegistersResource.getMinValue() != null ? loadRegistersResource.getMinValue() : null;
		Long max = loadRegistersResource.getMaxValue() != null ? loadRegistersResource.getMaxValue() : null;
		Long multiplier = loadRegistersResource.getMultiplier() != null ? loadRegistersResource.getMultiplier() : null;
		
		CardRegisterElement cardRegisterElement = new CardRegisterElement();
		cardRegisterElement.setDeviceId(deviceId);
		cardRegisterElement.setName(name);
		cardRegisterElement.setAddress(address);
		cardRegisterElement.setCount(count);
		cardRegisterElement.setIsRead(isRead);
		cardRegisterElement.setIsWrite(isWrite);
		cardRegisterElement.setType(type);
		cardRegisterElement.setSuffix(suffix);
		cardRegisterElement.setMinValue(min);
		cardRegisterElement.setMaxValue(max);
		cardRegisterElement.setMultiplier(multiplier);
		return cardRegisterElement;
		
	}
}
