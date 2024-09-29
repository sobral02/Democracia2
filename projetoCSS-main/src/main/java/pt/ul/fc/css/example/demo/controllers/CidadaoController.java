package pt.ul.fc.css.example.demo.controllers;

import java.util.List;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import pt.ul.fc.css.example.demo.entities.Cidadao;
import pt.ul.fc.css.example.demo.entities.Delegado;
import pt.ul.fc.css.example.demo.repositories.CidadaoRepository;

@RestController
@RequestMapping("/api")
public class CidadaoController {
  private final CidadaoRepository cidadaoRepository;

  public CidadaoController(CidadaoRepository cidadaoRepository) {
    this.cidadaoRepository = cidadaoRepository;
  }

  @GetMapping("/cidadao/{id}")
  public Cidadao getCidadaoById(@PathVariable Integer id) {
    return this.cidadaoRepository.getCidadaoById(id);
  }

  @GetMapping("/cidadaos")
  public List<Cidadao> getCidadaos() {
    return this.cidadaoRepository.allCidadaos();
  }
  @GetMapping("/cidadaosSize")
  public int getCidadaosSize() {
    return this.cidadaoRepository.allCidadaos().size();
  }

  @PutMapping("/cidadao")
  public Integer putCidadao(@NonNull @RequestBody Cidadao cidadao) {
    Cidadao saved = cidadaoRepository.save(cidadao);
    return saved.getId();
  }

  @PostMapping("/cidadao")
  public Integer postCidadao(@RequestBody String nome) {
    Cidadao novoCidadao = new Cidadao(nome);
    Cidadao saved = cidadaoRepository.save(novoCidadao);
    return saved.getId();
  }

  @RequestMapping(value = "/index")
  public ModelAndView home() {
    return new ModelAndView("index");
  }
}
