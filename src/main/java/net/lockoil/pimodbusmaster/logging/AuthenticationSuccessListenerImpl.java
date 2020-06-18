package net.lockoil.pimodbusmaster.logging;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;


@Component
public class AuthenticationSuccessListenerImpl implements ApplicationListener<AuthenticationSuccessEvent> {
	
	private final Logger log = Logger.getLogger(this.getClass().getSimpleName());

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent appEvent) {
        AuthenticationSuccessEvent event = (AuthenticationSuccessEvent) appEvent;
        User user =  (User)event.getAuthentication().getPrincipal();
        if (user instanceof User) {
            log.info("USER " + user.toString());
        }
    }
}