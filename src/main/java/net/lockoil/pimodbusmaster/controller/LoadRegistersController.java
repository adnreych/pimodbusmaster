package net.lockoil.pimodbusmaster.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.swagger.annotations.ApiOperation;
import net.lockoil.pimodbusmaster.model.CardRegisterElement;
import net.lockoil.pimodbusmaster.model.Device;
import net.lockoil.pimodbusmaster.model.LoadRegistersResource;
import net.lockoil.pimodbusmaster.service.DeviceService;
import net.lockoil.pimodbusmaster.service.RegistersService;
import net.lockoil.pimodbusmaster.util.Common;

@CrossOrigin
@RestController
public class LoadRegistersController {
	
	@Autowired
	RegistersService registersService;
	
	@ApiOperation(value = "Добавление новых регистров в устройство")
	@PostMapping("/api/registers/load")
	  public ResponseEntity<String> loadRegisters(@RequestBody ArrayList<LoadRegistersResource> loadRegistersResources) {
		
		ArrayList<CardRegisterElement> cardRegisterElements = new ArrayList<>();
		
		for(LoadRegistersResource loadRegistersResource : loadRegistersResources) {
			cardRegisterElements.add(registersService.parseRegisterElement(loadRegistersResource));
		}
		
		registersService.saveRegisters(cardRegisterElements);
			
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/loadregisters").buildAndExpand(cardRegisterElements.size())
		        .toUri();
		return ResponseEntity.created(uri).build();
	  }
	
	@ApiOperation(value = "Возвращает список регистров устройства", response = CardRegisterElement.class, responseContainer = "List")
	@GetMapping("/api/device/{id}")
	  public List<CardRegisterElement> getDevice(@PathVariable(value="id") Long id) {	
		return registersService.getDeviceRegisters(id);
	  }
	
	@ApiOperation(value = "Изменение регистра устройства")
	@PutMapping("/api/registers/change")
	  public void changeRegister(@RequestBody LoadRegistersResource loadRegistersResource) {		
		CardRegisterElement cardRegisterElement = registersService.parseRegisterElement(loadRegistersResource);
		registersService.changeRegister(cardRegisterElement);		
	  }
	
	@ApiOperation(value = "Удаление регистра устройства")
	@DeleteMapping("/api/registers/delete/{id}")
	  public void deleteRegister(@PathVariable(value="id") Long id) {		
		registersService.deleteRegister(id);		
	  }
	
	@ApiOperation(value = "Добавление регистра устройства")
	@PostMapping("/api/registers/add")
	  public Long addRegister(@RequestBody LoadRegistersResource loadRegistersResource) {		
			CardRegisterElement cardRegisterElement = registersService.parseRegisterElement(loadRegistersResource);
			return registersService.addRegister(cardRegisterElement);
	  }
	
}
