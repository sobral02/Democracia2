package pt.ul.fc.css.example.demo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pt.ul.fc.css.example.demo.catalogs.DelegadoCatalog;
import pt.ul.fc.css.example.demo.catalogs.ProjetoLeiCatalog;
import pt.ul.fc.css.example.demo.catalogs.TemaCatalog;
import pt.ul.fc.css.example.demo.handlers.ApresentarProjetoLeiHandler;
import pt.ul.fc.css.example.demo.handlers.FecharProjetoLeiHandler;

@SpringBootTest
public class FecharProjetoLeiTest {

  @Autowired private DelegadoCatalog delegadoCatalog;
  @Autowired private TemaCatalog temaCatalog;
  @Autowired private ProjetoLeiCatalog projetoLeiCatalog;
  @Autowired private FecharProjetoLeiHandler handler; // PRECISA DE ESTAR AQUI PARA O @SCHEDULED
  @Autowired private ApresentarProjetoLeiHandler handler1;

  @Test
  void DataExpiradaTest() throws InterruptedException {

    int sizeAtual = this.projetoLeiCatalog.getProjetosLei().size();
    this.delegadoCatalog.addDelegado("Johnny");

    // Cria-se um objecto ProjetoLei com uma data limite de 2 segundos, espera-se 5 e verifica-se se
    // já foi removido do repo
    int idDel = this.delegadoCatalog.getDelegadoByName("Johnny").getId();

    int id =
        this.handler1.proporProjetoLei(
            idDel,
            "EDUCACAO",
            "Propinas",
            "Diminuicao das propinas",
            "TestFiles/propinas.pdf",
            2L * 1000);
    assertEquals(sizeAtual + 1, this.projetoLeiCatalog.getProjetosLei().size());
    assertTrue(this.projetoLeiCatalog.existsProjetoLei("Propinas"));
    assertThat(this.projetoLeiCatalog.getProjetoLeiById(id).getId().equals(id));
    Thread.sleep(5000); // ESPERA 5 segundos
    assertEquals(sizeAtual, this.projetoLeiCatalog.getProjetosLei().size());
    assertFalse(this.projetoLeiCatalog.existsProjetoLei("Propinas"));
    this.delegadoCatalog.deleteDelegadoById(idDel);
  }
}
