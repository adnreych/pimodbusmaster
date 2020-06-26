package net.lockoil.pimodbusmaster.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import net.lockoil.pimodbusmaster.model.Device;
import net.lockoil.pimodbusmaster.service.DeviceService;

@CrossOrigin
@RestController
public class DeviceController {
	
	private final Logger log = Logger.getLogger(this.getClass().getSimpleName());
		
	@Autowired
	DeviceService deviceService;
	
	@PostMapping("/api/savedevice")
	  public Long saveDevice(@RequestBody Device device) {	
		deviceService.save(device);
		
		log.debug("SAVEDEVICE " + device.toString());
			
		return device.getId();
	  }
	
	@GetMapping("/api/devices")
	  public List<Device> getDevices() {		
		return deviceService.findAll();
	  }

}
