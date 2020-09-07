package net.lockoil.pimodbusmaster.logging;
	
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.session.SessionDestroyedEvent;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

/**
 * Логгер успешного выхода
 */
@Component
public class LogoutListenerImpl implements ApplicationListener<LogoutSuccessEvent> {
	
	private final Logger log = Logger.getLogger(this.getClass().getSimpleName());
 
    @Override
    public void onApplicationEvent(LogoutSuccessEvent appEvent) {
    	LogoutSuccessEvent event = (LogoutSuccessEvent) appEvent;
        User user =  (User)event.getAuthentication().getPrincipal();
        if (user instanceof User) {
            log.info("USER " + user.toString());
        }
    }
}