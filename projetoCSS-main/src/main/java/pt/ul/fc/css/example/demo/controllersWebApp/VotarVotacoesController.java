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
import pt.ul.fc.css.example.demo.entities.Votacao;
import pt.ul.fc.css.example.demo.entities.VotoPublico;
import pt.ul.fc.css.example.demo.handlers.EncontrarDelegadoParaVotarHandler;
import pt.ul.fc.css.example.demo.handlers.VotarPropostaHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/dashboard/votarVotacoes")
public class VotarVotacoesController {

    private final RestTemplate restTemplate;

    @Autowired
    private HttpSession session;

    @Autowired
    private VotarPropostaHandler votarPropostaHandler;

    @Autowired
    private EncontrarDelegadoParaVotarHandler encontrarDelegadoParaVotarHandler;


    public VotarVotacoesController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping
    public String showPage(Model model) {

        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/login";
        }

        ResponseEntity<List<Votacao>> votacoes = restTemplate.exchange(
                "http://localhost:8080/api/votacoes",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {});


        model.addAttribute("tipoUser", session.getAttribute("tipoUser"));
        model.addAttribute("userName", session.getAttribute("userName"));
        model.addAttribute("userId", session.getAttribute("loggedInUser"));
        model.addAttribute("votacoes", votacoes.getBody()); // getBody para receber as Votacoes
        int idUser = Integer.parseInt((String) session.getAttribute("loggedInUser"));


        List<Votacao> listaVotacoes = votacoes.getBody();
        Map<Votacao, String> paresVotoDelegadoVotacao = new HashMap<>();

        /*
        *  Breve resumo desta lógica:
        *
        * Para cada votação na base de dados encontra-se o delegado do utilizador mais adequado para votar nesta proposta
        * Se não existir, não há voto por omissão.
        * Se existir obtêm-se o voto público que pode ser nulo pois o delegado pode ainda não ter votado na proposta.
        * Nesse caso a aplicação mostra que não possui voto por omissão
        * Se existir mostra o valor do voto, o nome e o id do Delegado que vota pelo utilizador.
        *
        *
        * */

        if(listaVotacoes != null ){

            for(Votacao votacao : listaVotacoes){

                Delegado delegado  = encontrarDelegadoParaVotarHandler.encontra(votacao.getId(), idUser);

                if(delegado!=null){

                    VotoPublico votoPublico = restTemplate.exchange(
                            "http://localhost:8080/api/VotosPublicos/delegado/"+delegado.getId()+"/votacao/"+votacao.getId(),
                            HttpMethod.GET,
                            null,
                            new ParameterizedTypeReference<VotoPublico>() {}).getBody();


                    if(votoPublico!=null){

                        if (votoPublico.getValor()) {
                            paresVotoDelegadoVotacao.put(votacao, "O seu voto por omissão é a favor da proposta! \n" +
                                    " O seu delegado para esta proposta é " + delegado.getNome()+" com id "+ delegado.getId());
                        }else{
                            paresVotoDelegadoVotacao.put(votacao, "O seu voto por omissão é contra a proposta! \n" +
                                    " O seu delegado para esta proposta é " + delegado.getNome()+" com id "+ delegado.getId());
                        }

                    }else{
                        paresVotoDelegadoVotacao.put(votacao, "Não possui voto por omissão! O seu delegado ainda não votou nesta proposta!");
                    }

                }else{
                    paresVotoDelegadoVotacao.put(votacao, "Não possui voto por omissão! Não possui delegado para votar nesta proposta!");
                }

                if(votacao.cidadaoJaVotou(idUser)){
                    paresVotoDelegadoVotacao.put(votacao, "Já votou nesta proposta! Não possui voto por omissão!");
                }

            }
        }

        model.addAttribute("paresVotoDelegadoVotacao", paresVotoDelegadoVotacao.entrySet());

        return "votarVotacoes";
    }

    @PostMapping
    public String votar(@RequestParam("votId") Integer votId, @RequestParam("valor") Integer valor, RedirectAttributes redirectAttributes) {

        //Se valor for 1, voto a favor. Se for 0 voto contra.
        boolean val = valor == 1;

        int idUser = Integer.parseInt((String) session.getAttribute("loggedInUser"));

        boolean sucessoAoVotar = votarPropostaHandler.votarProposta(idUser,votId,val);

        if (sucessoAoVotar) {
            redirectAttributes.addAttribute("sucesso", true);
        } else {
            redirectAttributes.addAttribute("erro", true);
        }

        return "redirect:/dashboard/votarVotacoes";
    }






}
