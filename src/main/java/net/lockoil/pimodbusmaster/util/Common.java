package net.lockoil.pimodbusmaster.util;

import java.util.HashMap;
import java.util.Map;

import net.lockoil.pimodbusmaster.model.CardRegisterElement;
import net.lockoil.pimodbusmaster.model.LoadRegistersResource;

public class Common {
	public static final String HSTORE_TYPE = "net.lockoil.spicetrackerrest.util.HStoreType";
	
	public static CardRegisterElement parseRegisterElement(LoadRegistersResource loadRegistersResource) {
		
		Long deviceId = loadRegistersResource.getDeviceId();
		String name = loadRegistersResource.getName();
		Map<String, String> metadata = new HashMap<>();
		metadata.putIfAbsent("address", loadRegistersResource.getAddress().toString());
		metadata.putIfAbsent("count", loadRegistersResource.getCount().toString());
		metadata.putIfAbsent("isWrite", loadRegistersResource.getIsWrite().toString());
		metadata.putIfAbsent("isRead", loadRegistersResource.getIsRead().toString());
		metadata.putIfAbsent("type", loadRegistersResource.getType());
		String suffix = loadRegistersResource.getSuffix() != null ? loadRegistersResource.getSuffix() : null;
		Long min = loadRegistersResource.getMinValue() != null ? loadRegistersResource.getMinValue() : null;
		Long max = loadRegistersResource.getMaxValue() != null ? loadRegistersResource.getMaxValue() : null;
		Long multiplier = loadRegistersResource.getMultiplier() != null ? loadRegistersResource.getMultiplier() : null;
		
		CardRegisterElement cardRegisterElement = new CardRegisterElement();
		cardRegisterElement.setDeviceId(deviceId);
		cardRegisterElement.setName(name);
		cardRegisterElement.setRegisterMetadata(metadata);
		cardRegisterElement.setSuffix(suffix);
		cardRegisterElement.setMinValue(min);
		cardRegisterElement.setMaxValue(max);
		cardRegisterElement.setMultiplier(multiplier);
		return cardRegisterElement;
		
	}
}
