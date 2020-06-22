package net.lockoil.pimodbusmaster.controller;

import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
	
    private RequestCache requestCache = new HttpSessionRequestCache();
 
   /* @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
    	// здесь ищем юзера и если все ок передаем ответ, иначе юзер не найден с ошибкой
        return "login";
    }  */
    
    
}
