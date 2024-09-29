package pt.ul.fc.css.example.demo.catalogs;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.ul.fc.css.example.demo.entities.ProjetoLei;
import pt.ul.fc.css.example.demo.entities.Tema;
import pt.ul.fc.css.example.demo.handlers.FileHandler;
import pt.ul.fc.css.example.demo.repositories.ProjetoLeiRepository;

@Component
public class ProjetoLeiCatalog {

  @Autowired private ProjetoLeiRepository projetoLeiRepository;

  @Autowired private DelegadoCatalog delegadoCatalog;

  // Singleton
  private static ProjetoLeiCatalog catalog = null;

  public static ProjetoLeiCatalog getCatalogo() {
    if (catalog == null) {
      catalog = new ProjetoLeiCatalog();
    }
    return catalog;
  }

  protected ProjetoLeiCatalog() {}

  public List<ProjetoLei> getProjetosLei() {
    return projetoLeiRepository.allProjetoLei();
  }

  public ProjetoLei getProjetoLeiByName(String title) {
    return projetoLeiRepository.getByTitle(title);
  }

  public boolean existsProjetoLei(String titulo) {
    return projetoLeiRepository.existsByTitle(titulo);
  }

  public void deleteProjetoLeiById(Integer id) {
    boolean deleteFile = this.projetoLeiRepository.getProjetoLeiById(id).getApoios().size() < 10000;

    if (deleteFile)
      FileHandler.deleteFile(this.projetoLeiRepository.getProjetoLeiById(id).getFicheiro());

    this.projetoLeiRepository.deleteById(id);
  }

  public Integer addProjetoLei(
      Integer id, Tema tema, String titulo, String descricao, String anexo, Date date) {
    if (this.delegadoCatalog.existsDelegado(id)) {
      ProjetoLei pj = new ProjetoLei(tema, titulo, descricao, anexo, date);
      pj.addApoios(id);
      this.projetoLeiRepository.save(pj);
      return pj.getId();
    } else {
      System.out.println("Este delegado não existe");
      return -1;
    }
  }

  public boolean addApoio(Integer idProjeto, Integer idCid) {
    ProjetoLei pj = this.projetoLeiRepository.getProjetoLeiById(idProjeto);
    if (pj.addApoios(idCid)) {
      this.projetoLeiRepository.save(pj);
      return true;
    } else {
      return false;
    }
  }

  public ProjetoLei getProjetoLeiById(int id) {
    return this.projetoLeiRepository.getProjetoLeiById(id);
  }

  public List<ProjetoLei> getProjetosLeiByTema(String temaName) {

    return this.projetoLeiRepository.getByNomeTema(temaName);
  }

  public List<ProjetoLei> getProjetosLeiExpirados() {
    return this.projetoLeiRepository.getProjetosLeiExpirados();
  }

  public List<ProjetoLei> getProjetosLeiQueObtiveramAprovacao() {
    return this.projetoLeiRepository.getProjetosLeiComAprovacao();
  }
}
