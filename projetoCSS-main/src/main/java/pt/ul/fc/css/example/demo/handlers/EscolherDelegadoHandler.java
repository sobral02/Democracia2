package pt.ul.fc.css.example.demo.handlers;

import jakarta.transaction.Transactional;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.ul.fc.css.example.demo.catalogs.CidadaoCatalog;
import pt.ul.fc.css.example.demo.catalogs.DelegadoCatalog;
import pt.ul.fc.css.example.demo.catalogs.TemaCatalog;
import pt.ul.fc.css.example.demo.entities.Cidadao;
import pt.ul.fc.css.example.demo.entities.Delegado;
import pt.ul.fc.css.example.demo.entities.Tema;

@Service
public class EscolherDelegadoHandler {

  @Autowired private DelegadoCatalog delegadoCatalog;

  @Autowired private CidadaoCatalog cidadaoCatalog;

  @Autowired private TemaCatalog temaCatalog;

  public EscolherDelegadoHandler() {}

  @Transactional
  public boolean EscolherDelegado(Integer cidId, Integer delId, String temaName) {
    Delegado del = this.delegadoCatalog.getDelegadoById(delId);
    Cidadao cid = this.cidadaoCatalog.getCidadaoById(cidId);
    Tema tema = this.temaCatalog.getTemaByName(temaName);
    if (cid.getDelegados().isEmpty()) {
      cid.addDelegado(tema,del);
      del.addCidadao(cid, tema);
      this.delegadoCatalog.updateDelegado(del);
      this.cidadaoCatalog.updateCidadao(cid);
      return true;
    } else {
      boolean existsDel = false;
      for (Map.Entry<String, Delegado> pair : cid.getDelegados().entrySet()) {
        if (pair.getKey().equals(tema.getNome())) {
          existsDel = true;
          System.out.println("Já existe delegado para esse tema");
          break;
        }
      }
      if (!existsDel) {
        cid.addDelegado(tema,del);
        del.addCidadao(cid, tema);
        this.delegadoCatalog.updateDelegado(del);
        this.cidadaoCatalog.updateCidadao(cid);
      }
      return !existsDel;
    }
  }
}
