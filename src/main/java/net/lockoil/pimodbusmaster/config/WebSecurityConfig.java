package net.lockoil.pimodbusmaster.config;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import net.lockoil.pimodbusmaster.service.UserDetailServiceImpl;



@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	private final Logger log = Logger.getLogger(this.getClass().getSimpleName());
	
    @Autowired
    private UserDetailServiceImpl userDetailsService;	
    
	
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	
        http
        .csrf().disable()   
        .authorizeRequests()
        .antMatchers("/**")
        			.permitAll()
                .and()
	                .formLogin()                
	                .loginPage("/index.html")
	                .loginProcessingUrl("/login_process")     
	                .failureHandler(customAuthenticationFailureHandler())
	                .permitAll();
        
    }
    
    @Bean
    public AuthenticationFailureHandler customAuthenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }
    
    	
    @Autowired
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
    	log.info("auth" + auth.toString() + " userservice " + userDetailsService.toString());
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder(10));
    }
     
}