package net.lockoil.pimodbusmaster.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import net.lockoil.pimodbusmaster.model.RegisterGroup;
import net.lockoil.pimodbusmaster.model.RegisterGroupResource;
import net.lockoil.pimodbusmaster.service.RegisterGroupService;

@CrossOrigin
@RestController
public class RegisterGroupController {
	
	private final Logger log = Logger.getLogger(this.getClass().getSimpleName());
	
	@Autowired
	RegisterGroupService registerGroupService;
	
	@ApiOperation(value = "Добавляет новую группу регистров", response = RegisterGroupResource.class, responseContainer = "List")
	@PostMapping("/api/registerGroup/save/")
	  public List<RegisterGroupResource> saveAll(@RequestBody List<RegisterGroup> registerGroup) {	
		log.debug("SaveRegGroup " + registerGroup.toString());
		return registerGroupService.saveAll(registerGroup);
	  }
	
	@ApiOperation(value = "Получить группу регистров по его id", response = RegisterGroup.class)
	@GetMapping("/api/registerGroup/{id}")
	  public RegisterGroup getRegisterGroup(@PathVariable(value="id") Long id) {		
		return registerGroupService.findById(id);
	  }
	
	@ApiOperation(value = "Получить список всех групп регистров", response = RegisterGroup.class, responseContainer = "List")
	@GetMapping("/api/registerGroup/")
	  public List<RegisterGroup> getAllRegisterGroup() {		
		return registerGroupService.findAll();
	  }

}
