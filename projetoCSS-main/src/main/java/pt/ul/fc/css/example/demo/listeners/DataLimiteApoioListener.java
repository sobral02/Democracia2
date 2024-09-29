package pt.ul.fc.css.example.demo.listeners;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pt.ul.fc.css.example.demo.catalogs.ProjetoLeiCatalog;
import pt.ul.fc.css.example.demo.entities.ProjetoLei;
import pt.ul.fc.css.example.demo.handlers.FecharProjetoLeiHandler;

@Component
public class DataLimiteApoioListener {

  @Autowired private FecharProjetoLeiHandler fecharProjetoLeiHandler;

  @Autowired private ProjetoLeiCatalog projetoLeiCatalog;

  @Scheduled(fixedRate = 3000) // check every 3 secs
  public void checkDate() {

    List<ProjetoLei> projetosLeiExpirados = projetoLeiCatalog.getProjetosLeiExpirados();
    for (ProjetoLei proj : projetosLeiExpirados) {
      fecharProjetoLeiHandler.apagarProjetoLeiById(proj.getId());
    }
  }
}
