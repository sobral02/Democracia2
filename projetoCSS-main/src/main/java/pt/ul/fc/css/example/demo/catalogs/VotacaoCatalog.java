package pt.ul.fc.css.example.demo.catalogs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.ul.fc.css.example.demo.entities.Cidadao;
import pt.ul.fc.css.example.demo.entities.ProjetoLei;
import pt.ul.fc.css.example.demo.entities.Votacao;
import pt.ul.fc.css.example.demo.handlers.FileHandler;
import pt.ul.fc.css.example.demo.repositories.VotacaoRepository;

import java.util.Date;
import java.util.List;

@Component
public class VotacaoCatalog {

    @Autowired
    private VotacaoRepository votacaoRepository;

    // Singleton
    private static VotacaoCatalog catalog = null;

    public static VotacaoCatalog getCatalogo() {
        if(catalog==null) {
            catalog = new VotacaoCatalog();
        }
        return catalog;
    }

    protected VotacaoCatalog() {}

    public List<Votacao> getVotacoes(){
        return votacaoRepository.allVotacoes();
    }

    public Votacao getVotacaoByTitle(String title){
        return votacaoRepository.getVotacaoByName(title);
    }


    public boolean existsVotacao(String title){
        return votacaoRepository.existsByTitle(title);
    }

    public Integer addVotacao(ProjetoLei pj){
        Votacao vot = new Votacao(pj);
        this.votacaoRepository.save(vot);
        return vot.getId();
    }

    public Votacao getVotacaoById(Integer id){
        return this.votacaoRepository.getVotacaoById(id);
    }
    public void deleteVotacaoById(Integer id){
        FileHandler.deleteFile(this.votacaoRepository.getVotacaoById(id).getPdf());
        this.votacaoRepository.deleteById(id);
    }

    public void updateVoto(Votacao vot) {
        this.votacaoRepository.save(vot);
    }

    public void setDateNow(Integer id){
        Votacao voto = this.votacaoRepository.getVotacaoById(id);
        voto.setDataExp(new Date());
        updateVoto(voto);
    }

    public List<Votacao> getVotacoesExpiradas() {
        return this.votacaoRepository.getVotacoesExpiradas();
    }

    public void aprovaVotacao(Integer id) {
        Votacao votacao = this.votacaoRepository.getVotacaoById(id);
        votacao.aprovaVotacao();
        this.votacaoRepository.save(votacao);
    }

    public void fechaVotacao(Integer id) {
        Votacao votacao = this.votacaoRepository.getVotacaoById(id);
        votacao.fechaVotacao();
        this.votacaoRepository.save(votacao);
    }


}
