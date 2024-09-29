package pt.ul.fc.css.example.demo.controllersWebApp;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebController {

    @RequestMapping("/")
    public String getLogin(Model model) {
        return "login";
    }
}
