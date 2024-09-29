package pt.ul.fc.css.example.demo.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pt.ul.fc.css.example.demo.catalogs.ProjetoLeiCatalog;
import pt.ul.fc.css.example.demo.entities.ProjetoLei;
import pt.ul.fc.css.example.demo.handlers.FecharProjetoLeiHandler;
import pt.ul.fc.css.example.demo.handlers.VotarPropostaHandler;

import java.util.List;

@Component
public class LimiteDezMilListener {


    @Autowired
    private ProjetoLeiCatalog projetoLeiCatalog;

    @Autowired
    private FecharProjetoLeiHandler fecharProjetoLeiHandler;

    @Scheduled(fixedRate = 3000)
    public void checkLimite(){

        List<ProjetoLei> projetosLeiAprovados = projetoLeiCatalog.getProjetosLeiQueObtiveramAprovacao();

        for (ProjetoLei proj : projetosLeiAprovados) {
            fecharProjetoLeiHandler.createVotacaoFromProjetoLei(proj.getId());
        }
    }


}
