package pt.ul.fc.css.example.demo.catalogs;

import java.util.List;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.ul.fc.css.example.demo.entities.Tema;
import pt.ul.fc.css.example.demo.repositories.TemaRepository;

@Component
public class TemaCatalog {

  @Autowired private TemaRepository temaRepository;

  // Singleton
  private static TemaCatalog catalog = null;

  public static TemaCatalog getCatalogo() {
    if (catalog == null) {
      catalog = new TemaCatalog();
    }
    return catalog;
  }

  protected TemaCatalog() {}

  public List<Tema> getTemas() {
    return temaRepository.allTemas();
  }

  public Tema getTemaByName(String nome) {
    return temaRepository.getByNome(nome);
  }
  public Tema getTemaById(Integer id){return temaRepository.getTemaById(id);}

  public void initTema() {
    if (temaRepository.allTemas().size() > 0) {
      return;
    }

    Tema educacao = new Tema("EDUCACAO", null);
    Tema saude = new Tema("SAUDE", null);
    temaRepository.save(educacao);
    temaRepository.save(saude);
    temaRepository.save(new Tema("ESCOLAS", educacao));
    temaRepository.save(new Tema("HOSPITAIS", saude));
  }

  @PostConstruct
  public void postConstruct(){
    initTema();
  }

  public boolean existsTema(String name) {
    return temaRepository.existsByTitle(name);
  }
}
