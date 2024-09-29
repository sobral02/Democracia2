package pt.ul.fc.css.example.demo.catalogs;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.ul.fc.css.example.demo.entities.Delegado;
import pt.ul.fc.css.example.demo.repositories.DelegadoRepository;

@Component
public class DelegadoCatalog {

  @Autowired private DelegadoRepository delegadoRepository;

  // Singleton
  private static DelegadoCatalog catalog = null;

  public static DelegadoCatalog getCatalogo() {
    if (catalog == null) {
      catalog = new DelegadoCatalog();
    }
    return catalog;
  }

  protected DelegadoCatalog() {}

  public List<Delegado> getDelegados() {
    return delegadoRepository.allDelegados();
  }

  public Delegado getDelegadoByName(String nome) {
    return delegadoRepository.getDelegadoByName(nome);
  }

  public Delegado getDelegadoById(Integer id) {
    return delegadoRepository.getDelegadoById(id);
  }

  public boolean existsDelegado(Integer id) {
    return delegadoRepository.isDelegado(id);
  }

  public int addDelegado(String nome) {
    Delegado delegado = new Delegado(nome);
    this.delegadoRepository.save(delegado);
    return delegado.getId();
  }

  public void deleteDelegadoById(Integer id) {
    this.delegadoRepository.deleteById(id);
  }

  public void updateDelegado(Delegado del) {
    delegadoRepository.save(del);
  }

  public void resetDelegadoForDeletion(Integer delId) {
    Delegado del = this.delegadoRepository.getDelegadoById(delId);
    del.getDelegados().clear();
    del.getCidadaos().clear();
    this.delegadoRepository.save(del);
  }
}
