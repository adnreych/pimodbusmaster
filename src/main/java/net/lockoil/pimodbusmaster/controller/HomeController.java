package net.lockoil.pimodbusmaster.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeController {
 
    @RequestMapping(value = "/")
    public String index() {
        return "index";
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public String login() {
        return "login";
    }
}
