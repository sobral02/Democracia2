package pt.ul.fc.css.example.demo.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.ul.fc.css.example.demo.entities.VotoPublico;
import pt.ul.fc.css.example.demo.repositories.VotoPublicoRepository;

@RestController
@RequestMapping("/api")
public class VotoPublicoController {
  private final VotoPublicoRepository votoPublicoRepository;

  public VotoPublicoController(VotoPublicoRepository votoPublicoRepository) {
    this.votoPublicoRepository = votoPublicoRepository;
  }

  @GetMapping("/VotosPublicos")
  public List<VotoPublico> allVotosPublicos() {
    return this.votoPublicoRepository.allVotosPublicos();
  }

  @GetMapping("VotosPublicos/delegado/{id}")
  public List<VotoPublico> getVotoPublicoByDelegado_Id(@PathVariable Integer id) {
    return this.votoPublicoRepository.getVotoPublicoByDelegado_Id(id);
  }

  @GetMapping("VotosPublicos/delegado/{delId}/votacao/{votId}")
  public VotoPublico getVotoPublicoByDelegadoandVotId( @PathVariable Integer delId, @PathVariable Integer votId){
    return this.votoPublicoRepository.getVotoPublicoByDelegadoAndVotId(delId,votId);
  }

}
