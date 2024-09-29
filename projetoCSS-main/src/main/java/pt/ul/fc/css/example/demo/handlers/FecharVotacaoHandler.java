package pt.ul.fc.css.example.demo.handlers;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.ul.fc.css.example.demo.catalogs.CidadaoCatalog;
import pt.ul.fc.css.example.demo.catalogs.VotacaoCatalog;
import pt.ul.fc.css.example.demo.catalogs.VotoPublicoCatalog;
import pt.ul.fc.css.example.demo.entities.Cidadao;
import pt.ul.fc.css.example.demo.entities.Delegado;
import pt.ul.fc.css.example.demo.entities.Tema;
import pt.ul.fc.css.example.demo.entities.Votacao;

@Component
public class FecharVotacaoHandler {

  @Autowired private VotacaoCatalog votacaoCatalog;

  @Autowired private CidadaoCatalog cidadaoCatalog;

  @Autowired private VotoPublicoCatalog votoPublicoCatalog;

  @Autowired private VotarPropostaHandler votarPropostaHandler;

  public FecharVotacaoHandler() {}

  public void fecharVotacao(Integer idVotacao) {

    Votacao votacao = votacaoCatalog.getVotacaoById(idVotacao);

    List<Cidadao> cidadaosQueNaoVotaram = cidadaoCatalog.getCidadaos();

    // Remover da lista de todos os cidadaos os que votaram

    for (Integer id : votacao.getCidVotaram()) {

      for (Cidadao cidadao : cidadaosQueNaoVotaram) {

        if (cidadao.getId() == id.intValue()) {
          cidadaosQueNaoVotaram.remove(cidadao);
          break;
        }
      }
    }

    // Propagação de votos
    for (Cidadao cidadao : cidadaosQueNaoVotaram) {

      Map<String, Delegado> delegadosDoCidadao = cidadao.getDelegados();

      // Se o mapa contiver um delegado com o tema da votacao, propaga-se o voto deste cidadao
      if (delegadosDoCidadao.containsKey(votacao.getTema().getNome())) {

        // Podia ser feito com uma query, mas muito complexa
        for (Map.Entry<String, Delegado> entry : delegadosDoCidadao.entrySet()) {

          // Se algum delegado tem o tema da votaçao, o voto deste passa a ser o do cidadao
          if (entry.getKey().equals(votacao.getTema().getNome())) {

            // Ver se o delegado já votou
            if (votacao.cidadaoJaVotou(entry.getValue().getId())) {
              if (votoPublicoCatalog.getVotoPublicoByDelegadoAndVotId(
                      entry.getValue().getId(), votacao.getId())
                  != null) {
                votarPropostaHandler.AddVoto(
                        cidadao.getId(),
                        votacao.getId(),
                        votoPublicoCatalog
                                .getVotoPublicoByDelegadoAndVotId(entry.getValue().getId(), votacao.getId())
                                .getValor());
              }
            }
          }
        }

      } else {
        // Não contendo vamos encontrar o delegado adequado a fazer a votacao, se não encontrarmos
        // não há voto

        Tema tema = votacao.getTema().getTemaPai();

        while (tema != null) {

          if (delegadosDoCidadao.containsKey(tema.getNome())) {
            // Podia ser feito com uma query, mas muito complexa
            for (Map.Entry<String, Delegado> entry : delegadosDoCidadao.entrySet()) {

              // Se algum delegado tem o tema da votaçao, o voto deste passa a ser o do cidadao
              if (entry.getKey().equals(tema.getNome())) {

                // Ver se o delegado já votou
                if (votacao.cidadaoJaVotou(entry.getValue().getId())) {
                  if (votoPublicoCatalog.getVotoPublicoByDelegadoAndVotId(
                          entry.getValue().getId(), votacao.getId())
                      != null) {
                    votarPropostaHandler.AddVoto(
                        cidadao.getId(),
                        votacao.getId(),
                        votoPublicoCatalog
                            .getVotoPublicoByDelegadoAndVotId(
                                entry.getValue().getId(), votacao.getId())
                            .getValor());
                  }
                }
              }
            }
          }

          tema = tema.getTemaPai();
        }
      }
    }

    if (votacao.getVotosFavor() > votacao.getVotosContra()) {
      votacaoCatalog.aprovaVotacao(votacao.getId());
    }

    votacaoCatalog.fechaVotacao(votacao.getId());
  }
}
