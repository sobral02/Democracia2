package pt.ul.fc.css.example.demo.listeners;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pt.ul.fc.css.example.demo.catalogs.VotacaoCatalog;
import pt.ul.fc.css.example.demo.entities.Votacao;
import pt.ul.fc.css.example.demo.handlers.FecharVotacaoHandler;

@Component
public class DataLimiteVotacaoListener {

  @Autowired private FecharVotacaoHandler fecharVotacaoHandler;

  @Autowired private VotacaoCatalog votacaoCatalog;

  @Scheduled(fixedRate = 3000) // check every 3 secs
  public void checkDate() {

    List<Votacao> votacoesExpiradas = votacaoCatalog.getVotacoesExpiradas();
    for (Votacao votacao : votacoesExpiradas) {
      if (!votacao.getFechado()) {
        fecharVotacaoHandler.fecharVotacao(votacao.getId());
      }
    }
  }
}
