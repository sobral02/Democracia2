package pt.ul.fc.css.example.demo.controllersWebApp;

import jakarta.servlet.http.HttpSession;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pt.ul.fc.css.example.demo.entities.Delegado;
import pt.ul.fc.css.example.demo.entities.Tema;
import pt.ul.fc.css.example.demo.handlers.ApresentarProjetoLeiHandler;

@Controller
@RequestMapping("/dashboard/apresentarProjetoLei")
public class ApresentarProjetoLeiController {

  @Autowired private HttpSession session;

  @Autowired private ApresentarProjetoLeiHandler apresentarProjetoLeiHandler;

  private final RestTemplate restTemplate;

  public ApresentarProjetoLeiController(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  @GetMapping
  public String showPage(Model model) {

    if (session.getAttribute("loggedInUser") == null) {
      return "redirect:/login";
    }

    model.addAttribute("userName", session.getAttribute("userName"));

    return "apresentarProjetoLei";
  }

  @PostMapping
  public String apresentarProjeto(
      @RequestParam("tema") String tema,
      @RequestParam("titulo") String titulo,
      @RequestParam("textoDescritivo") String descricao,
      @RequestParam("data") String data,
      @RequestParam("hora") String hora,
      RedirectAttributes redirectAttributes) {

    String userId = (String) session.getAttribute("loggedInUser");

    String dateTimeString = data + " " + hora;
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    Date dateTime;
    try {

      dateTime = dateFormat.parse(dateTimeString);
      Date currentDate = new Date();
      Date oneYearLater = new Date(currentDate.getTime());
      oneYearLater.setYear(oneYearLater.getYear() + 1);

      if (dateTime.before(new Date())) {
        redirectAttributes.addAttribute("dataPassada", true);

      } else if (dateTime.before(oneYearLater)) {

        ResponseEntity<Tema> tema_ =
            restTemplate.exchange(
                "http://localhost:8080/api/Tema/nome/" + tema,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {});

        if (tema_.getBody() != null) {

          ResponseEntity<Delegado> delegado_ =
              restTemplate.exchange(
                  "http://localhost:8080/api/delegado/" + userId,
                  HttpMethod.GET,
                  null,
                  new ParameterizedTypeReference<>() {});

          if (delegado_.getBody() != null) {

            int resultado =
                apresentarProjetoLeiHandler.proporProjetoLei(
                    Integer.parseInt(userId),
                    tema,
                    titulo,
                    descricao,
                    "src/main/resources/teste.pdf",
                    dateTime.getTime());
            if (resultado != -1) {

              redirectAttributes.addAttribute("sucessoAoApresentar", true);

            } else {
              redirectAttributes.addAttribute("tituloJaExiste", true);
            }

          } else {
            redirectAttributes.addAttribute("userNaoEDelegado", true);
          }

        } else {
          redirectAttributes.addAttribute("naoExisteTema", true);
        }

      } else {
        redirectAttributes.addAttribute("dataExcede", true);
      }

    } catch (ParseException e) {
      e.printStackTrace();
    }

    return "redirect:/dashboard/apresentarProjetoLei";
  }
}
