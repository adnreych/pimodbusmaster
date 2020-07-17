package net.lockoil.pimodbusmaster.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import net.lockoil.pimodbusmaster.model.User;
import net.lockoil.pimodbusmaster.service.UserDetailServiceImpl;

@CrossOrigin
@RestController
public class AdminController {

	@Autowired
	UserDetailServiceImpl userDetailServiceImpl;
	
	@GetMapping("/api/allusers")
	  public List<User> getDevices() {		
		return userDetailServiceImpl.allUsers();
	  }
	
}
