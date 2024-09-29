package pt.ul.fc.css.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pt.ul.fc.css.example.demo.catalogs.CidadaoCatalog;
import pt.ul.fc.css.example.demo.catalogs.DelegadoCatalog;
import pt.ul.fc.css.example.demo.handlers.EscolherDelegadoHandler;

@SpringBootTest
public class EscolherDelegadoTests {

  @Autowired private CidadaoCatalog cidadaoCatalog;
  @Autowired private DelegadoCatalog delegadoCatalog;
  @Autowired private EscolherDelegadoHandler handler;

  @Test
  void EscolherDelegadoTest() {

    this.cidadaoCatalog.addCidadao("Johnny");
    this.delegadoCatalog.addDelegado("Joaquim");
    int idDel = this.delegadoCatalog.getDelegadoByName("Joaquim").getId();
    int idCid = this.cidadaoCatalog.getCidadaoByName("Johnny").getId();

    this.handler.EscolherDelegado(idCid, idDel, "EDUCACAO");
    assertEquals(
        idDel,
        this.cidadaoCatalog
            .getCidadaoById(idCid)
            .getDelegados().entrySet().iterator().next().getValue().getId());
    assertEquals(
        idCid,
        this.delegadoCatalog
            .getDelegadoById(idDel)
            .getCidadaos()
            .keySet()
            .iterator()
            .next()
            .getId());

    this.cidadaoCatalog.resetCidadaoForDeletion(idCid);
    this.delegadoCatalog.deleteDelegadoById(idDel);
    this.cidadaoCatalog.deleteCidadaoById(idCid);
  }

  @Test
  void EscolherDelegadoExistenteTest() {

    this.cidadaoCatalog.addCidadao("Johnny");
    this.delegadoCatalog.addDelegado("Joaquim");
    this.delegadoCatalog.addDelegado("Andre");

    int idCid = this.cidadaoCatalog.getCidadaoByName("Johnny").getId();
    int idDel = this.delegadoCatalog.getDelegadoByName("Joaquim").getId();
    int idDel2 = this.delegadoCatalog.getDelegadoByName("Andre").getId();

    this.handler.EscolherDelegado(idCid, idDel, "EDUCACAO");
    assertEquals(
        idDel,
        this.cidadaoCatalog
            .getCidadaoById(idCid)
            .getDelegados()
            .entrySet()
            .iterator()
            .next()
            .getValue().getId());
    assertEquals(
        idCid,
        this.delegadoCatalog
            .getDelegadoById(idDel)
            .getCidadaos()
            .keySet()
            .iterator()
            .next()
            .getId());
    this.handler.EscolherDelegado(idCid, idDel2, "EDUCACAO");
    assertEquals(
        idDel,
        this.cidadaoCatalog
            .getCidadaoById(idCid)
            .getDelegados()
            .entrySet()
            .iterator()
            .next()
            .getValue().getId());
    assertNotEquals(
        idDel2,
        this.cidadaoCatalog
            .getCidadaoById(idCid)
            .getDelegados()
            .entrySet()
            .iterator()
            .next()
            .getValue().getId());
    assertEquals(
        idCid,
        this.delegadoCatalog
            .getDelegadoById(idDel)
            .getCidadaos()
            .keySet()
            .iterator()
            .next()
            .getId());

    this.cidadaoCatalog.resetCidadaoForDeletion(idCid);
    this.delegadoCatalog.deleteDelegadoById(idDel);
    this.delegadoCatalog.deleteDelegadoById(idDel2);
    this.cidadaoCatalog.deleteCidadaoById(idCid);
  }
}
