package pt.ul.fc.css.example.demo.controllersWebApp;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pt.ul.fc.css.example.demo.entities.Delegado;
import pt.ul.fc.css.example.demo.entities.Tema;
import pt.ul.fc.css.example.demo.handlers.EscolherDelegadoHandler;


@Controller
@RequestMapping("/dashboard/escolherDelegado")
public class EscolherDelegadoController {

    @Autowired
    private HttpSession session;

    @Autowired
    private EscolherDelegadoHandler escolherDelegadoHandler;

    private final RestTemplate restTemplate;

    public EscolherDelegadoController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    @GetMapping
    public String showPage(Model model) {

        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/login";
        }

        model.addAttribute("userName", session.getAttribute("userName"));

        return "escolherDelegado";
    }

    @PostMapping
    public String escolherDelegado(@RequestParam("tema") String tema, @RequestParam("idDelegado") String idDelegado, RedirectAttributes redirectAttributes) {

        int userId = Integer.parseInt((String) session.getAttribute("loggedInUser"));

        ResponseEntity<Tema> tema_ = restTemplate.exchange(
                "http://localhost:8080/api/Tema/nome/"+tema,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {});

        if(userId == Integer.parseInt(idDelegado)){
            redirectAttributes.addAttribute("naoPodeEscolherASiMesmo", true);
            return "redirect:/dashboard/escolherDelegado";
        }

        if(tema_.getBody() != null){

            ResponseEntity<Delegado> delegado_ = restTemplate.exchange(
                    "http://localhost:8080/api/delegado/"+idDelegado,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {});

            if(delegado_.getBody()!=null){

                boolean success = escolherDelegadoHandler.EscolherDelegado(userId,Integer.parseInt(idDelegado),tema);

                if(success){
                    redirectAttributes.addAttribute("sucesso", true);
                }else{
                    redirectAttributes.addAttribute("delegadoJaExiste", true);
                }

            }else{
                redirectAttributes.addAttribute("naoExisteDelegado", true);

            }
            return "redirect:/dashboard/escolherDelegado";

        }else{
            redirectAttributes.addAttribute("naoExisteTema", true);
        }
        return "redirect:/dashboard/escolherDelegado";
    }

}
