package pt.ul.fc.css.example.demo.handlers;

import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.ul.fc.css.example.demo.catalogs.DelegadoCatalog;
import pt.ul.fc.css.example.demo.catalogs.ProjetoLeiCatalog;
import pt.ul.fc.css.example.demo.catalogs.TemaCatalog;
import pt.ul.fc.css.example.demo.entities.Delegado;
import pt.ul.fc.css.example.demo.entities.Tema;

@Service
public class ApresentarProjetoLeiHandler {
  @Autowired private ProjetoLeiCatalog projetoLeiCatalog;
  @Autowired private DelegadoCatalog delegadoCatalog;
  @Autowired private TemaCatalog temaCatalog;

  public int proporProjetoLei(
      Integer delId,
      String temaName,
      String titulo,
      String descricao,
      String anexo,
      Long milisecs) {
    Delegado del = this.delegadoCatalog.getDelegadoById(delId);
    Tema tema = this.temaCatalog.getTemaByName(temaName);

    if (this.projetoLeiCatalog.existsProjetoLei(titulo)) {
      System.out.println("Esse projeto lei já existe!");
      return -1;
    } else {
      return this.projetoLeiCatalog.addProjetoLei(
          del.getId(),
          tema,
          titulo,
          descricao,
          anexo,
          new Date(System.currentTimeMillis() + milisecs));
    }
  }
}
