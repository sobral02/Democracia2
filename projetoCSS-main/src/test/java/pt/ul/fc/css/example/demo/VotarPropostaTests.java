package pt.ul.fc.css.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pt.ul.fc.css.example.demo.catalogs.*;
import pt.ul.fc.css.example.demo.handlers.ApresentarProjetoLeiHandler;
import pt.ul.fc.css.example.demo.handlers.EscolherDelegadoHandler;
import pt.ul.fc.css.example.demo.handlers.VotarPropostaHandler;

@SpringBootTest
public class VotarPropostaTests {

  @Autowired private CidadaoCatalog cidadaoCatalog;
  @Autowired private DelegadoCatalog delegadoCatalog;
  @Autowired private VotoPublicoCatalog votoPublicoCatalog;
  @Autowired private VotacaoCatalog votacaoCatalog;
  @Autowired private ProjetoLeiCatalog projetoLeiCatalog;
  @Autowired private ApresentarProjetoLeiHandler handler1;
  @Autowired private VotarPropostaHandler handler2;
  @Autowired private EscolherDelegadoHandler handler3;

  @Test
  public void VotarPropostaTest() {
    this.cidadaoCatalog.addCidadao("Jack");
    this.delegadoCatalog.addDelegado("Joaquim");
    int idCid = this.cidadaoCatalog.getCidadaoByName("Jack").getId();
    int idDel = this.delegadoCatalog.getDelegadoByName("Joaquim").getId();

    handler1.proporProjetoLei(
        idDel, "SAUDE", "Ambulancias", "teste", "TestFiles/teste.pdf", 50L * 10000);
    handler1.proporProjetoLei(
        idDel, "ESCOLAS", "Autocarros escolares", "teste", "TestFiles/propinas.pdf", 10L * 10000);

    this.votacaoCatalog.addVotacao(this.projetoLeiCatalog.getProjetoLeiByName("Ambulancias"));
    this.votacaoCatalog.addVotacao(
        this.projetoLeiCatalog.getProjetoLeiByName("Autocarros escolares"));
    handler3.EscolherDelegado(idCid, idDel, "SAUDE");

    int idVot1 = this.votacaoCatalog.getVotacaoByTitle("Ambulancias").getId();
    int idVot2 = this.votacaoCatalog.getVotacaoByTitle("Autocarros escolares").getId();

    handler2.votarProposta(idCid, idVot1, false); // false - nao favoravel , true - favoravel
    handler2.votarProposta(idDel, idVot1, true);
    handler2.votarProposta(idCid, idVot2, true);
    handler2.votarProposta(idDel, idVot2, true);

    assertEquals(
        this.votacaoCatalog.getVotacaoById(idVot1).getVotosContra(),
        this.votacaoCatalog.getVotacaoById(idVot1).getVotosFavor());
    // verifica se o numero de votações contra e favor eh a msm

    assertEquals(
        1,
        this.votacaoCatalog
            .getVotacaoById(idVot1)
            .getVotosContra()); // verifica votos a favor na votação1
    assertEquals(
        1,
        this.votacaoCatalog
            .getVotacaoById(idVot1)
            .getVotosFavor()); // verifica votos a contra na votação1

    assertEquals(
        2,
        this.votacaoCatalog.getVotacaoById(idVot2).getVotosContra()
            + this.votacaoCatalog.getVotacaoById(idVot2).getVotosFavor());
    assertEquals(2, this.votacaoCatalog.getVotacaoById(idVot2).getVotosFavor());
    assertEquals(0, this.votacaoCatalog.getVotacaoById(idVot2).getVotosContra());

    assertEquals(true, this.votacaoCatalog.getVotacaoById(idVot1).cidadaoJaVotou(idCid));
    // como cid ja votou nesta votaçao nao pode votar mais nela pelo o que os votos a favor
    // mantem-se iguais...
    int votosTotaisVot1 =
        this.votacaoCatalog.getVotacaoById(idVot1).getVotosFavor()
            + this.votacaoCatalog.getVotacaoById(idVot1).getVotosContra();
    handler2.votarProposta(idCid, idVot1, true);
    assertEquals(
        1,
        this.votacaoCatalog
            .getVotacaoById(idVot1)
            .getVotosFavor()); // verifica que n de votos manteve-se pois o cid ja votou nesta
    // votação

    this.cidadaoCatalog.resetCidadaoForDeletion(idCid);
    this.votoPublicoCatalog.deleteVotoPublicoById(
        this.votoPublicoCatalog.getVotoPublicoByDelegadoAndVotId(idDel, idVot2).getId());
    this.votoPublicoCatalog.deleteVotoPublicoById(
        this.votoPublicoCatalog.getVotoPublicoByDelegadoAndVotId(idDel, idVot1).getId());
    this.delegadoCatalog.deleteDelegadoById(idDel);
    this.cidadaoCatalog.deleteCidadaoById(idCid);
    this.projetoLeiCatalog.deleteProjetoLeiById(
        this.projetoLeiCatalog.getProjetoLeiByName("Ambulancias").getId());
    this.projetoLeiCatalog.deleteProjetoLeiById(
        this.projetoLeiCatalog.getProjetoLeiByName("Autocarros escolares").getId());
    this.votacaoCatalog.deleteVotacaoById(idVot1);
    this.votacaoCatalog.deleteVotacaoById(idVot2);
  }
}
