package pt.ul.fc.css.example.demo.controllersWebApp;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pt.ul.fc.css.example.demo.entities.Cidadao;
import pt.ul.fc.css.example.demo.entities.Delegado;

@Controller
public class LoginController {

    @Autowired
    private HttpSession session;

    private final RestTemplate restTemplate;

    public LoginController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("id") String id, RedirectAttributes redirectAttributes) {

        // Fazer pedido get à API
        ResponseEntity< Cidadao > cidadao = restTemplate.exchange(
                "http://localhost:8080/api/cidadao/"+id,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });
        boolean existeCidadao = cidadao.getBody() != null;

        ResponseEntity<Delegado> delegado = restTemplate.exchange(
                "http://localhost:8080/api/delegado/"+id,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });
        boolean existeDelegado = delegado.getBody() != null;

        if (existeDelegado) {

            session.setAttribute("tipoUser", "Delegado");
            session.setAttribute("userName", delegado.getBody().getNome());
            session.setAttribute("loggedInUser", id);

            return "redirect:/dashboard";

        } else if (existeCidadao) {

            session.setAttribute("tipoUser", "Cidadao");
            session.setAttribute("userName", cidadao.getBody().getNome());
            session.setAttribute("loggedInUser", id);

            return "redirect:/dashboard";

        } else {
            redirectAttributes.addAttribute("error", true);
            return "redirect:/login";
        }
    }

}