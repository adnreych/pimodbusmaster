package net.lockoil.pimodbusmaster.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
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
	
	
	@ApiOperation(value = "Возвращает всех пользователей", response = User.class, responseContainer = "List")
	@GetMapping("/api/allusers")
	  public List<User> getAllUsers() {		
		return userDetailServiceImpl.allUsers();
	  }
	
	@ApiOperation(value = "Создает нового пользователя и возвращает его id", response = Long.class)
	@PostMapping("/api/adduser")
	  public Long addUser(@RequestBody User user) {			
		return userDetailServiceImpl.saveUser(user);
	  }
	
	@ApiOperation(value = "Возвращает роли пользователей", response = Role.class, responseContainer = "List")
	@GetMapping("/api/allroles")
	  public List<Role> getAllRoles() {		
		return rolesService.findAll();
	  }
	
}
