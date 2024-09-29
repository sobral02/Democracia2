package pt.ul.fc.css.example.demo.catalogs;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.ul.fc.css.example.demo.entities.Delegado;
import pt.ul.fc.css.example.demo.entities.Votacao;
import pt.ul.fc.css.example.demo.entities.VotoPublico;
import pt.ul.fc.css.example.demo.repositories.VotoPublicoRepository;

@Component
public class VotoPublicoCatalog {

  @Autowired private VotoPublicoRepository votoPublicoRepository;

  // Singleton
  private static VotoPublicoCatalog catalog = null;

  public static VotoPublicoCatalog getCatalogo() {
    if (catalog == null) {
      catalog = new VotoPublicoCatalog();
    }
    return catalog;
  }

  protected VotoPublicoCatalog() {}

  public List<VotoPublico> getVotosPublicosByTitle(String titulo) {
    return votoPublicoRepository.getVotosPublicoByTitulo(titulo);
  }

  public List<VotoPublico> getVotosPublicosDeDelegado(Integer delegadoId) {
    return votoPublicoRepository.getVotoPublicoByDelegado_Id(delegadoId);
  }

  public void addVotoPublico(Delegado del, Votacao vot, boolean valor) {
    VotoPublico vp = new VotoPublico(del, vot, valor);
    this.votoPublicoRepository.save(vp);
  }

  public VotoPublico getVotoPublicoByDelegadoAndVotId(Integer idDel, Integer idVot) {
    return this.votoPublicoRepository.getVotoPublicoByDelegadoAndVotId(idDel, idVot);
  }

  public void deleteVotoPublicoById(Integer id) {
    this.votoPublicoRepository.deleteById(id);
  }

  public void deleteVotoPublicoByDelegadoAndVotId(Integer idDel, Integer idVot) {
    this.votoPublicoRepository.deleteById(getVotoPublicoByDelegadoAndVotId(idDel, idVot).getId());
  }
}
