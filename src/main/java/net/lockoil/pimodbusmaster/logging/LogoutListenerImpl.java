package net.lockoil.pimodbusmaster.logging;
	
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.session.SessionDestroyedEvent;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
 
@Component
public class LogoutListenerImpl implements ApplicationListener<SessionDestroyedEvent> {
	
	private final Logger log = Logger.getLogger(this.getClass().getSimpleName());
 
    @Override
    public void onApplicationEvent(SessionDestroyedEvent event) {
        List<SecurityContext> lstSecurityContext = event.getSecurityContexts();
        User user;
        for (SecurityContext securityContext : lstSecurityContext) {
            user = (User) securityContext.getAuthentication().getPrincipal();
            if (user instanceof User) {
            	log.info("USER " + user.toString());
            }
        }
    }
}