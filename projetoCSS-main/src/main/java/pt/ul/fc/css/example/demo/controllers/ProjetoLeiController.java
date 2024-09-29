package pt.ul.fc.css.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import pt.ul.fc.css.example.demo.entities.ProjetoLei;
import pt.ul.fc.css.example.demo.handlers.ApoiarProjetoLeiHandler;
import pt.ul.fc.css.example.demo.handlers.ApresentarProjetoLeiHandler;
import pt.ul.fc.css.example.demo.repositories.ProjetoLeiRepository;

@RestController
@RequestMapping("/api")
public class ProjetoLeiController {
  private final ProjetoLeiRepository ProjetoLeiRepository;
  @Autowired
  private ApoiarProjetoLeiHandler apoiarProjetoLeiHandler;

  public ProjetoLeiController(ProjetoLeiRepository projetoLeiRepository) {
    this.ProjetoLeiRepository = projetoLeiRepository;
  }

  @GetMapping("/projetosLei")
  public List<ProjetoLei> getProjetosLei() {

    return this.ProjetoLeiRepository.allProjetoLei();
  }

  @GetMapping("/ProjetoLei/id/{id}")
  public ProjetoLei getProjetoLeiID(@PathVariable Integer id) {
    return this.ProjetoLeiRepository.getProjetoLeiById(id);
  }

  @GetMapping("/ProjetoLei/titulo/{titulo}")
  public ProjetoLei getProjetoLeiTitulo(@PathVariable String titulo) {
    return this.ProjetoLeiRepository.getByTitle(titulo);
  }

  @GetMapping("/ProjetoLei/tema/{tema}")
  public List<ProjetoLei> getProjetoLeiTema(@PathVariable String tema) {
    return this.ProjetoLeiRepository.getByNomeTema(tema);
  }

  @PutMapping("/ProjetoLei/{id}/{cidId}")
  public boolean putCidadao(@PathVariable Integer id,@PathVariable Integer cidId) {
    return apoiarProjetoLeiHandler.addApoio(id,cidId);
  }
}
