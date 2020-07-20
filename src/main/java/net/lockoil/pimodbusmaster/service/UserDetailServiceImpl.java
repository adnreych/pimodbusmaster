package net.lockoil.pimodbusmaster.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import net.lockoil.pimodbusmaster.model.Role;
import net.lockoil.pimodbusmaster.model.User;
import net.lockoil.pimodbusmaster.repository.RoleRepository;
import net.lockoil.pimodbusmaster.repository.UserRepository;


/**
 * This class is used by spring security to authenticate and authorize user
 **/
@Service
public class UserDetailServiceImpl implements UserDetailsService  {
	private final Logger log = Logger.getLogger(this.getClass().getSimpleName());
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;

	@Autowired
	public UserDetailServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
	}
	
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {   
    	User user = userRepository.findByUsername(username);	
    	if(user == null){
            throw new UsernameNotFoundException("User not found.");
        }
    	log.info("CURRUSER" + user.toString());
    	UserDetails userDetails = new org.springframework.security.core.userdetails.User(username, user.getPassword(), 
    			new ArrayList<>(user.getRoles()));
        return userDetails;
    }   
    
    public User findUserById(Long userId) {
        Optional<User> userFromDb = userRepository.findById(userId);
        return userFromDb.orElse(new User());
    }

    public List<User> allUsers() {
        return userRepository.findAll();
    }

    public Long saveUser(User user) {
        return userRepository.save(user).getId();
    }

    public boolean deleteUser(Long userId) {
        if (userRepository.findById(userId).isPresent()) {
            userRepository.deleteById(userId);
            return true;
        }
        return false;
    }
} 