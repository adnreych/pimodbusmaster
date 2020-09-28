package net.lockoil.pimodbusmaster.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import jssc.SerialPortException;
import net.lockoil.pimodbusmaster.model.AtConnectionRequest;
import net.lockoil.pimodbusmaster.model.Device;
import net.lockoil.pimodbusmaster.model.Role;
import net.lockoil.pimodbusmaster.service.DeviceService;

@CrossOrigin
@RestController
public class DeviceController {
	
	private final Logger log = Logger.getLogger(this.getClass().getSimpleName());
		
	@Autowired
	DeviceService deviceService;
	
	@ApiOperation(value = "Добавляет новое устройство, возвращаего его id", response = Device.class)
	@PostMapping("/api/savedevice/")
	  public Device saveDevice(@RequestBody Device device) {	
		log.debug("SAVEDEVICE " + device.toString());
		return deviceService.save(device);
	  }
	
	@ApiOperation(value = "Возвращает список всех устройств", response = Device.class, responseContainer = "List")
	@GetMapping("/api/devices/")
	  public List<Device> getDevices() {		
		return deviceService.findAll();
	  }
	
	@ApiOperation(value = "Удаляет устройство по его id", response = Device.class, responseContainer = "List")
	@DeleteMapping("/api/deviceDelete/{id}")
	  public void deleteDevice(@PathVariable(value="id") Long id) {		
		deviceService.delete(id);
	  }
	
	@ApiOperation(value = "Запрос на модемное соединение с устройством", response = String.class)
	@PostMapping("/api/csdConnect/")
	  public String atConnect(@RequestBody AtConnectionRequest atConnectionRequest) throws SerialPortException {	
		return deviceService.atConnect(atConnectionRequest);
	  }
	
	@ApiOperation(value = "Разрыв модемного соединения")
	@PostMapping("/api/csdDisconnect/")
	  public boolean atDisconnect(@RequestBody AtConnectionRequest atConnectionRequest) throws SerialPortException {	
		return deviceService.atDisconnect(atConnectionRequest);
	  }
	
	@ApiOperation(value = "Возвращает список текущих COM-портов на сервере")
	@GetMapping("/api/ports/")
	  public String[] getPorts() {	
		return deviceService.getPorts();
	  }

	

}
