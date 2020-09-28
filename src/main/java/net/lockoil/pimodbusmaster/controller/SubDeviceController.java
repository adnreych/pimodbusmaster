package net.lockoil.pimodbusmaster.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import net.lockoil.pimodbusmaster.model.SubDevice;
import net.lockoil.pimodbusmaster.service.SubDeviceService;

@CrossOrigin
@RestController
public class SubDeviceController {
	
	@Autowired
	SubDeviceService subDeviceService;
	
	@ApiOperation(value = "Добавляет новые дочерние устройства, возвращая его id", response = SubDevice.class, responseContainer = "List")
	@PostMapping("/api/savesubdevices/")
	  public List<SubDevice> saveSubDevice(@RequestBody List<SubDevice> subDevices) {	
		return subDeviceService.saveAll(subDevices);
	  }

}
