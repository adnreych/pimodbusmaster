package net.lockoil.pimodbusmaster.starthooks;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

import net.lockoil.pimodbusmaster.model.Role;
import net.lockoil.pimodbusmaster.repository.RoleRepository;

/**
 * Действия при старте приложения
 */
@Component
public class StartHooks implements CommandLineRunner {
	
		@Autowired
		RoleRepository roleRepository;

  	 
  	    @Override
  	    public void run(String...args) throws Exception {
  	    	
  	        List<String> currentRoles = Lists
							  	        	.newArrayList(roleRepository.findAll())
							  	        	.stream()
							  	        	.map(e -> e.getName())
							  	        	.collect(Collectors.toList());
  	        
  	        List<String> newRolesNames = Arrays
						  	        		.stream(new String [] {"ADMIN", "EMPLOYEE", "CLIENT", "CLIENTCUT"})
						  	        		.filter(e -> !currentRoles.contains(e))
						  	        		.collect(Collectors.toList());
  	        
  	        // заполняем роли, если они не заполнены
  	        if (!newRolesNames.isEmpty()) {
  	        	newRolesNames
	        		.stream()
	        		.map(e -> roleRepository.save(new Role(e)))
	        		.collect(Collectors.toList());
  	        }	        
  	        
  	    }
  	}