package pt.ul.fc.css.example.demo.controllersWebApp;



import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import pt.ul.fc.css.example.demo.entities.ProjetoLei;

import java.util.List;

@Controller
@RequestMapping("/dashboard/consultarProjetosLei")
public class ConsultarProjetosLeiController {
    private final RestTemplate restTemplate;

    @Autowired
    private HttpSession session;

    public ConsultarProjetosLeiController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping
    public String showPage(Model model) {

        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/login";
        }

        ResponseEntity<List<ProjetoLei>> projetosLei = restTemplate.exchange(
                "http://localhost:8080/api/projetosLei",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {});

        model.addAttribute("tipoUser", session.getAttribute("tipoUser"));
        model.addAttribute("userName", session.getAttribute("userName"));
        model.addAttribute("userId", session.getAttribute("loggedInUser"));
        model.addAttribute("projetosLei", projetosLei.getBody());

        return "consultarProjetosLei";
    }




}