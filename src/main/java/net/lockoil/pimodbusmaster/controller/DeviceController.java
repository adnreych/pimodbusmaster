package net.lockoil.pimodbusmaster.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jssc.SerialPortException;
import net.lockoil.pimodbusmaster.model.AtConnectionRequest;
import net.lockoil.pimodbusmaster.model.CardRegisterElement;
import net.lockoil.pimodbusmaster.model.Device;
import net.lockoil.pimodbusmaster.model.LoadRegistersResource;
import net.lockoil.pimodbusmaster.service.DeviceService;

@CrossOrigin
@RestController
public class DeviceController {
	
	private final Logger log = Logger.getLogger(this.getClass().getSimpleName());
		
	@Autowired
	DeviceService deviceService;
	
	@PostMapping("/api/savedevice/")
	  public Long saveDevice(@RequestBody Device device) {	
		log.debug("SAVEDEVICE " + device.toString());
		return deviceService.save(device).getId();
	  }
	
	@GetMapping("/api/devices/")
	  public List<Device> getDevices() {		
		return deviceService.findAll();
	  }
	
	@DeleteMapping("/api/deviceDelete/{id}")
	  public void deleteDevice(@PathVariable(value="id") Long id) {		
		deviceService.delete(id);
	  }
	
	@PostMapping("/api/csdConnect/")
	  public String atConnect(@RequestBody AtConnectionRequest atConnectionRequest) throws SerialPortException {	
		return deviceService.atConnect(atConnectionRequest);
	  }
	
	@PostMapping("/api/csdDisonnect/")
	  public boolean atDisconnect(@RequestBody AtConnectionRequest atConnectionRequest) throws SerialPortException {	
		return deviceService.atDisconnect(atConnectionRequest);
	  }
	

}
