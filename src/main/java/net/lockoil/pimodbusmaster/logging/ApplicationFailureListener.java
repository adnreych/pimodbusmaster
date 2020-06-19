package net.lockoil.pimodbusmaster.logging;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

@Component
public class ApplicationFailureListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {
	
	private final Logger log = Logger.getLogger(this.getClass().getSimpleName());

    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent appEvent) {
    	AuthenticationFailureBadCredentialsEvent event = (AuthenticationFailureBadCredentialsEvent) appEvent;
        log.info("AUTH FAILURE " + event.getException().getClass().getSimpleName());
        log.info("AUTH FAILURE " + event.getAuthentication().getName());
    }
}