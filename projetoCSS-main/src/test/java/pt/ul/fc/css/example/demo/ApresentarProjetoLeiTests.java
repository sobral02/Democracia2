package pt.ul.fc.css.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pt.ul.fc.css.example.demo.catalogs.DelegadoCatalog;
import pt.ul.fc.css.example.demo.catalogs.ProjetoLeiCatalog;
import pt.ul.fc.css.example.demo.catalogs.TemaCatalog;
import pt.ul.fc.css.example.demo.handlers.ApresentarProjetoLeiHandler;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ApresentarProjetoLeiTests {

  @Autowired private ProjetoLeiCatalog projetoLeiCatalog;
  @Autowired private DelegadoCatalog delegadoCatalog;
  @Autowired private TemaCatalog temaCatalog;
  @Autowired private ApresentarProjetoLeiHandler handler;

  @Test
  void ProporProjetoLeiTest() {
    this.temaCatalog.initTema();
    int sizeAtual = this.projetoLeiCatalog.getProjetosLei().size();

    this.delegadoCatalog.addDelegado("Johnny");
    int idDel = this.delegadoCatalog.getDelegadoByName("Johnny").getId();
    int id =
        this.handler.proporProjetoLei(
            idDel,
            "EDUCACAO",
            "Propinas",
            "Diminuicao das propinas",
            "TestFiles/propinas.pdf",
            System.currentTimeMillis() + 3 * 10000);

    assertEquals(sizeAtual + 1, this.projetoLeiCatalog.getProjetosLei().size());
    this.delegadoCatalog.deleteDelegadoById(idDel);
    this.projetoLeiCatalog.deleteProjetoLeiById(id);
    assertEquals(sizeAtual, this.projetoLeiCatalog.getProjetosLei().size());

  }

  @Test
  void FindByIDTest() {
    this.temaCatalog.initTema();
    this.delegadoCatalog.addDelegado("Richards");
    int idDel = this.delegadoCatalog.getDelegadoByName("Richards").getId();
    int id =
        this.handler.proporProjetoLei(
            idDel,
            "ESCOLAS",
            "Propinas2",
            "Aumento das propinas",
            "TestFiles/propinas2.pdf",
            System.currentTimeMillis() + 3 * 10000);

    assertEquals(id, this.projetoLeiCatalog.getProjetoLeiById(id).getId());
    assertEquals(idDel, this.projetoLeiCatalog.getProjetoLeiById(id).getApoios().get(0));
    this.projetoLeiCatalog.deleteProjetoLeiById(id);
    this.delegadoCatalog.deleteDelegadoById(idDel);
  }

  @Test
  void FindByTitleTest() {

    this.delegadoCatalog.addDelegado("Richards");

    int idDel = this.delegadoCatalog.getDelegadoByName("Richards").getId();
    int id =
        this.handler.proporProjetoLei(
            idDel,
            "SAUDE",
            "Propinas2",
            "Aumento das propinas",
            "TestFiles/propinas2.pdf",
            System.currentTimeMillis() + 3 * 10000);

    assertNotEquals(
        "Propinas", this.projetoLeiCatalog.getProjetoLeiByName("Propinas2").getTitulo());
    assertEquals("Propinas2", this.projetoLeiCatalog.getProjetoLeiByName("Propinas2").getTitulo());
    assertEquals(
        this.projetoLeiCatalog.getProjetoLeiByName("Propinas2").getTitulo(),
        this.projetoLeiCatalog.getProjetoLeiById(id).getTitulo());
    assertEquals(this.projetoLeiCatalog.getProjetoLeiByName("Propinas2").getApoios().get(0), idDel);
    this.projetoLeiCatalog.deleteProjetoLeiById(id);

    this.delegadoCatalog.deleteDelegadoById(idDel);
  }

  @Test
  void FindByTemaTest() {
    this.delegadoCatalog.addDelegado("Richards");
    int idDel = this.delegadoCatalog.getDelegadoByName("Richards").getId();
    String tema = "EDUCACAO";
    int sizeAtual = this.projetoLeiCatalog.getProjetosLeiByTema(tema).size();
    int id =
        this.handler.proporProjetoLei(
            idDel,
            tema,
            "Propinas2",
            "Aumento das propinas",
            "TestFiles/propinas2.pdf",
            System.currentTimeMillis() + 3 * 10000);

    assertEquals(sizeAtual + 1, this.projetoLeiCatalog.getProjetosLeiByTema(tema).size());
    assertEquals(
        this.temaCatalog.getTemaByName(tema),
        this.projetoLeiCatalog.getProjetosLeiByTema(tema).get(sizeAtual).getTema());

    this.projetoLeiCatalog.deleteProjetoLeiById(id);
    this.delegadoCatalog.deleteDelegadoById(idDel);
  }

  @Test
  void allProjetosLeiTest() {

    int sizeAtual = this.projetoLeiCatalog.getProjetosLei().size();

    this.delegadoCatalog.addDelegado("Richards");
    int idDel1 = this.delegadoCatalog.getDelegadoByName("Richards").getId();
    String tema1 = "EDUCACAO";
    int id1 =
        this.handler.proporProjetoLei(
            idDel1,
            tema1,
            "Propinas",
            "Diminuicao das propinas",
            "TestFiles/propinas.pdf",
            System.currentTimeMillis() + 3 * 10000);

    assertTrue(this.projetoLeiCatalog
            .getProjetosLei()
            .get(sizeAtual)
            .getApoios()
            .contains(idDel1)); // verfica se o delegado1 esta nos apoios do projetoLei1

    String tema2 = "ESCOLAS";

    this.delegadoCatalog.addDelegado("Goncalino");
    int idDel2 = this.delegadoCatalog.getDelegadoByName("Goncalino").getId();
    int id2 =
        this.handler.proporProjetoLei(
            idDel2,
            tema2,
            "Veiculos Escolares",
            "Mais Veiculos",
            "TestFiles/veiculos.pdf",
            System.currentTimeMillis() + 3 * 10000);

    assertFalse(this.projetoLeiCatalog
            .getProjetosLei()
            .get(sizeAtual + 1)
            .getApoios()
            .contains(idDel1)); // verfica se o delegado1 esta nos apoios do projetoLei2
    assertTrue(this.projetoLeiCatalog
            .getProjetosLei()
            .get(sizeAtual + 1)
            .getApoios()
            .contains(idDel2)); // verfica se o delegado2 esta nos apoios do projetoLei2

    assertEquals(sizeAtual + 2, this.projetoLeiCatalog.getProjetosLei().size());
    this.projetoLeiCatalog.deleteProjetoLeiById(id1);
    this.projetoLeiCatalog.deleteProjetoLeiById(id2);

    this.delegadoCatalog.deleteDelegadoById(idDel1);
    this.delegadoCatalog.deleteDelegadoById(idDel2);
  }
}
