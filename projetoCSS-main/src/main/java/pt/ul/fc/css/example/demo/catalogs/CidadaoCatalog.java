package pt.ul.fc.css.example.demo.catalogs;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.ul.fc.css.example.demo.entities.Cidadao;
import pt.ul.fc.css.example.demo.repositories.CidadaoRepository;

@Component
public class CidadaoCatalog {

  @Autowired private CidadaoRepository cidadaoRepository;

  // Singleton
  private static CidadaoCatalog catalog = null;

  public static CidadaoCatalog getCatalogo() {
    if (catalog == null) {
      catalog = new CidadaoCatalog();
    }
    return catalog;
  }

  protected CidadaoCatalog() {}

  public Integer addCidadao(String nome) {
    Cidadao cidadao = new Cidadao(nome);
    this.cidadaoRepository.save(cidadao);
    return cidadao.getId();
  }

  public List<Cidadao> getCidadaos() {
    return cidadaoRepository.allCidadaos();
  }

  public Cidadao getCidadaoByName(String nome) {
    return cidadaoRepository.findByName(nome);
  }

  public boolean existsCidadao(Integer id) {
    return cidadaoRepository.existsById(id);
  }

  public void updateCidadao(Cidadao cid) {
    this.cidadaoRepository.save(cid);
  }

  public void deleteCidadaoById(Integer id) {
    this.cidadaoRepository.deleteById(id);
  }

  public Cidadao getCidadaoById(Integer id) {
    return this.cidadaoRepository.getCidadaoById(id);
  }

  public void resetCidadaoForDeletion(Integer id) {
    Cidadao cid = this.cidadaoRepository.getCidadaoById(id);
    cid.getDelegados().clear();
    this.cidadaoRepository.save(cid);
  }
}
