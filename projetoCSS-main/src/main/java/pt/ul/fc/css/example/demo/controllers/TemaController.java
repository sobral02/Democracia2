package pt.ul.fc.css.example.demo.controllers;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import pt.ul.fc.css.example.demo.entities.Tema;
import pt.ul.fc.css.example.demo.repositories.TemaRepository;

@RestController
@RequestMapping("/api")
public class TemaController {
  private final TemaRepository temaRepository;

  public TemaController(TemaRepository temaRepository) {
    this.temaRepository = temaRepository;
  }

  @GetMapping("/Tema")
  public List<Tema> getTemas() {

    return this.temaRepository.allTemas();
  }

  @GetMapping("/Tema/id/{id}")
  public Tema getTemaID(@PathVariable Integer id) {
    return this.temaRepository.getTemaById(id);
  }

  @GetMapping("/Tema/nome/{nome}")
  public Tema getTemaNome(@PathVariable String nome) {
    return this.temaRepository.getByNome(nome);
  }
  /*
  @PutMapping("/ProjetoLei")
  public Integer putTema(@NonNull @RequestBody Tema tema) {
      Tema saved = this.temaRepository.save(tema);
      return saved.getId();
  }*/
}
