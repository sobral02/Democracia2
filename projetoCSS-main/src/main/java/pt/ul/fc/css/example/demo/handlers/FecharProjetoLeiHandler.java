package pt.ul.fc.css.example.demo.handlers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.ul.fc.css.example.demo.catalogs.ProjetoLeiCatalog;
import pt.ul.fc.css.example.demo.catalogs.VotacaoCatalog;
import pt.ul.fc.css.example.demo.entities.ProjetoLei;

import java.util.List;

@Service
public class FecharProjetoLeiHandler {

    @Autowired
    private ProjetoLeiCatalog projetoLeiCatalog;

    @Autowired
    private VotacaoCatalog votacaoCatalog;

    public FecharProjetoLeiHandler(){}


    public List<ProjetoLei> allProjetosLei() {
        return this.projetoLeiCatalog.getProjetosLei();
    }

    public void apagarProjetoLeiById(Integer id) {
        this.projetoLeiCatalog.deleteProjetoLeiById(id);
    }

    public void createVotacaoFromProjetoLei(Integer id){
        votacaoCatalog.addVotacao(this.projetoLeiCatalog.getProjetoLeiById(id));
        apagarProjetoLeiById(id);
    }

}
