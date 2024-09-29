package pt.ul.fc.css.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pt.ul.fc.css.example.demo.catalogs.*;
import pt.ul.fc.css.example.demo.handlers.ApresentarProjetoLeiHandler;
import pt.ul.fc.css.example.demo.handlers.EscolherDelegadoHandler;
import pt.ul.fc.css.example.demo.handlers.VotarPropostaHandler;

@SpringBootTest
public class FecharVotacaoTest {

  @Autowired private VotacaoCatalog votacaoCatalog;

  @Autowired private DelegadoCatalog delegadoCatalog;

  @Autowired private ProjetoLeiCatalog projetoLeiCatalog;

  @Autowired private VotoPublicoCatalog votoPublicoCatalog;

  @Autowired private CidadaoCatalog cidadaoCatalog;

  @Autowired private TemaCatalog temaCatalog;

  @Autowired private EscolherDelegadoHandler escolherDelegadoHandler;

  @Autowired private VotarPropostaHandler votarPropostaHandler;

  @Autowired private ApresentarProjetoLeiHandler apresentarProjetoLeiHandler;

  @Test
  void dataExpirada() throws InterruptedException {

    // Adicionar cidadãos ao teste. 2 vão votar a favor, 1 contra. Um dos votos a favor vai ser
    // propagado por um delegado.

    int cid1ID = cidadaoCatalog.addCidadao("Afonso");
    int cid2ID = cidadaoCatalog.addCidadao("João");
    int cid3ID = cidadaoCatalog.addCidadao("Joana");
    int delID = delegadoCatalog.addDelegado("Alberto");
    int delAutorID = delegadoCatalog.addDelegado("Ricardo");

    // Associar o delegado ao cidadao 1
    escolherDelegadoHandler.EscolherDelegado(cid1ID, delID, "EDUCACAO");
    apresentarProjetoLeiHandler.proporProjetoLei(
        delAutorID,
        "ESCOLAS",
        "Hospital Sta Maria",
        "a",
        "./TestFiles/fase1.pdf",
        System.currentTimeMillis() + 500000);

    int idVot =
        votacaoCatalog.addVotacao(projetoLeiCatalog.getProjetoLeiByName("Hospital Sta Maria"));

    // Adiciona o voto do delegado
    votarPropostaHandler.votarProposta(delID, idVot, true);
    votarPropostaHandler.votarProposta(cid2ID, idVot, false);
    votarPropostaHandler.votarProposta(cid3ID, idVot, true);

    // Para não ter que esperar 2 semanas
    votacaoCatalog.setDateNow(idVot);

    Thread.sleep(5000);

    assertEquals(3, votacaoCatalog.getVotacaoById(idVot).getVotosFavor());
    assertEquals(1, votacaoCatalog.getVotacaoById(idVot).getVotosContra());

    assertTrue(votacaoCatalog.getVotacaoById(idVot).getFechado());
    assertTrue(votacaoCatalog.getVotacaoById(idVot).getAprovado());

    votoPublicoCatalog.deleteVotoPublicoByDelegadoAndVotId(delID, idVot);
    votacaoCatalog.deleteVotacaoById(idVot);

    projetoLeiCatalog.deleteProjetoLeiById(
        projetoLeiCatalog.getProjetoLeiByName("Hospital Sta Maria").getId());

    delegadoCatalog.resetDelegadoForDeletion(delID);
    delegadoCatalog.resetDelegadoForDeletion(delAutorID);
    cidadaoCatalog.resetCidadaoForDeletion(cid3ID);
    cidadaoCatalog.resetCidadaoForDeletion(cid1ID);
    cidadaoCatalog.resetCidadaoForDeletion(cid2ID);

    cidadaoCatalog.deleteCidadaoById(cid1ID);
    cidadaoCatalog.deleteCidadaoById(cid2ID);
    cidadaoCatalog.deleteCidadaoById(cid3ID);
    delegadoCatalog.deleteDelegadoById(delAutorID);
    delegadoCatalog.deleteDelegadoById(delID);
  }
}
