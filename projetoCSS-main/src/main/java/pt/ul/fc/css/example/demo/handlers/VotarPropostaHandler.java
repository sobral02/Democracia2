package pt.ul.fc.css.example.demo.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.ul.fc.css.example.demo.catalogs.CidadaoCatalog;
import pt.ul.fc.css.example.demo.catalogs.DelegadoCatalog;
import pt.ul.fc.css.example.demo.catalogs.VotacaoCatalog;
import pt.ul.fc.css.example.demo.catalogs.VotoPublicoCatalog;
import pt.ul.fc.css.example.demo.entities.Cidadao;
import pt.ul.fc.css.example.demo.entities.Delegado;
import pt.ul.fc.css.example.demo.entities.Votacao;

@Service
public class VotarPropostaHandler {
  @Autowired private CidadaoCatalog cidadaoCatalog;
  @Autowired private VotacaoCatalog votacaoCatalog;
  @Autowired private DelegadoCatalog delegadoCatalog;
  @Autowired private VotoPublicoCatalog votoPublicoCatalog;

  public VotarPropostaHandler() {}

  public boolean votarProposta(Integer cidId, Integer idVot, boolean valor) {
    Cidadao cid = this.cidadaoCatalog.getCidadaoById(cidId);
    Votacao vot = this.votacaoCatalog.getVotacaoById(idVot);
    boolean voted = vot.getCidVotaram().contains(cid.getId());
    if (voted) {
      return false;
    } else {
      RealizarVoto(cid, idVot, valor);
      return true;
    }
  }

  private void RealizarVoto(Cidadao cid, Integer votId, boolean valor) {
    boolean isDel = this.delegadoCatalog.existsDelegado(cid.getId());
    if (isDel) {
      AddVotoPublico(cid.getId(), votId, valor);
    } else {
      AddVoto(cid.getId(), votId, valor);
    }
  }

  private void AddVotoPublico(Integer id, Integer votId, boolean valor) {
    Delegado del = this.delegadoCatalog.getDelegadoById(id);
    this.votoPublicoCatalog.addVotoPublico(del, votacaoCatalog.getVotacaoById(votId), valor);
    Votacao vot = this.votacaoCatalog.getVotacaoById(votId);
    vot.addVoto(id, valor);
    this.votacaoCatalog.updateVoto(vot);
  }

  public void AddVoto(Integer id, Integer votId, boolean valor) {
    Votacao vot = this.votacaoCatalog.getVotacaoById(votId);
    vot.addVoto(id, valor);
    this.votacaoCatalog.updateVoto(vot);
  }
}
