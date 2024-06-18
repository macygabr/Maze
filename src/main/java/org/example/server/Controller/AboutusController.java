package org.example.server.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AboutusController {
    @RequestMapping("/aboutus")
    public String getFild(String message) {
        return "pages/aboutus";
    }
}