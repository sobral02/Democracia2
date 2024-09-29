package pt.ul.fc.css.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pt.ul.fc.css.example.demo.catalogs.*;

@SpringBootTest
class ListarVotacoesTests {

  @Autowired private ProjetoLeiCatalog projetoLeiCatalog;
  @Autowired private VotacaoCatalog votacaoCatalog;
  @Autowired private TemaCatalog temaCatalog;
  @Autowired private DelegadoCatalog delegadoCatalog;

  @Test
  void testGetAllVotacoes() {
    this.temaCatalog.initTema();
    this.delegadoCatalog.addDelegado("Johnny");
    int idDel = this.delegadoCatalog.getDelegadoByName("Johnny").getId();

    this.projetoLeiCatalog.addProjetoLei(
        idDel,
        this.temaCatalog.getTemaByName("EDUCACAO"),
        "teste",
        "teste",
        "TestFiles/teste.pdf",
        new Date(System.currentTimeMillis() + 50L * 10000));

    int before = this.votacaoCatalog.getVotacoes().size();

    this.votacaoCatalog.addVotacao(this.projetoLeiCatalog.getProjetoLeiByName("teste"));
    assertEquals(before+1, this.votacaoCatalog.getVotacoes().size());

    assertEquals("teste", this.votacaoCatalog.getVotacaoByTitle("teste").getTitulo());

    this.votacaoCatalog.deleteVotacaoById(this.votacaoCatalog.getVotacaoByTitle("teste").getId());

    assertEquals(before, this.votacaoCatalog.getVotacoes().size());
    this.projetoLeiCatalog.deleteProjetoLeiById(
        this.projetoLeiCatalog.getProjetoLeiByName("teste").getId());
  }
}
