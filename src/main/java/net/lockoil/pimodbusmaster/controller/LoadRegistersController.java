package net.lockoil.pimodbusmaster.controller;

import java.net.URI;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import net.lockoil.pimodbusmaster.model.CardRegisterElement;
import net.lockoil.pimodbusmaster.model.Device;
import net.lockoil.pimodbusmaster.model.LoadRegistersResource;
import net.lockoil.pimodbusmaster.service.DeviceService;
import net.lockoil.pimodbusmaster.service.LoadNewCardService;
import net.lockoil.pimodbusmaster.util.Common;

@CrossOrigin
@RestController
public class LoadRegistersController {
	
	@Autowired
	LoadNewCardService loadNewCardService;
	
	@PostMapping("/api/loadregisters")
	  public ResponseEntity<String> loadRegisters(@RequestBody ArrayList<LoadRegistersResource> loadRegistersResources) {
		
		ArrayList<CardRegisterElement> cardRegisterElements = new ArrayList<>();
		
		for(LoadRegistersResource loadRegistersResource : loadRegistersResources) {
			cardRegisterElements.add(loadNewCardService.parseRegisterElement(loadRegistersResource));
		}
		
		loadNewCardService.saveRegisters(cardRegisterElements);
			
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/loadregisters").buildAndExpand(cardRegisterElements.size())
		        .toUri();
		return ResponseEntity.created(uri).build();
	  }
	
}
