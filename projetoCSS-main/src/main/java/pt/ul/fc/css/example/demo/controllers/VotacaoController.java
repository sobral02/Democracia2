package pt.ul.fc.css.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pt.ul.fc.css.example.demo.entities.Votacao;
import pt.ul.fc.css.example.demo.handlers.VotarPropostaHandler;
import pt.ul.fc.css.example.demo.repositories.VotacaoRepository;

@RestController
@RequestMapping("/api")
public class VotacaoController {
  private final VotacaoRepository votacaoRepository;
  @Autowired
  private VotarPropostaHandler handler;

  public VotacaoController(VotacaoRepository votacaoRepository) {
    this.votacaoRepository = votacaoRepository;
  }

  @GetMapping("/votacoes")
  public List<Votacao> getVotacoes() {
    return this.votacaoRepository.allVotacoes();
  }

  @GetMapping("/votacao/id/{id}")
  public Votacao getVotacaoById(@PathVariable Integer id) {
    return this.votacaoRepository.getVotacaoById(id);
  }

  @PutMapping("/votacao/{votId}/{cidId}")
  public boolean realizarVoto(@PathVariable Integer votId,@PathVariable Integer cidId,@RequestBody String valor){
    boolean trueValor = Boolean.parseBoolean(valor);
    return handler.votarProposta(cidId,votId,trueValor);
  }
}
