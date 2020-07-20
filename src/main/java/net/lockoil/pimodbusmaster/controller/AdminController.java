package net.lockoil.pimodbusmaster.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import net.lockoil.pimodbusmaster.model.Device;
import net.lockoil.pimodbusmaster.model.Role;
import net.lockoil.pimodbusmaster.model.User;
import net.lockoil.pimodbusmaster.service.RolesService;
import net.lockoil.pimodbusmaster.service.UserDetailServiceImpl;

@CrossOrigin
@RestController
public class AdminController {

	@Autowired
	UserDetailServiceImpl userDetailServiceImpl;
	
	@Autowired
	RolesService rolesService;
	
	
	@GetMapping("/api/allusers")
	  public List<User> getAllUsers() {		
		return userDetailServiceImpl.allUsers();
	  }
	
	@PostMapping("/api/adduser")
	  public Long addUser(@RequestBody User user) {			
		return userDetailServiceImpl.saveUser(user);
	  }
	
	@GetMapping("/api/allroles")
	  public List<Role> getAllRoles() {		
		return rolesService.findAll();
	  }
	
}
