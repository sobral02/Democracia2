package pt.ul.fc.css.example.demo.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.ul.fc.css.example.demo.catalogs.CidadaoCatalog;
import pt.ul.fc.css.example.demo.catalogs.VotacaoCatalog;
import pt.ul.fc.css.example.demo.entities.Cidadao;
import pt.ul.fc.css.example.demo.entities.Delegado;
import pt.ul.fc.css.example.demo.entities.Tema;
import pt.ul.fc.css.example.demo.entities.Votacao;

import java.util.Map;


@Component
public class EncontrarDelegadoParaVotarHandler {

    @Autowired
    private VotacaoCatalog votacaoCatalog;
    @Autowired
    private CidadaoCatalog cidadaoCatalog;

    public Delegado encontra(Integer idVotacao, Integer idCidadao){

        Votacao votacao = votacaoCatalog.getVotacaoById(idVotacao);
        Cidadao cid = cidadaoCatalog.getCidadaoById(idCidadao);
        Map<String, Delegado> delegados = cid.getDelegados();
        Tema tema = votacao.getTema();

        // Itera pelos temas até encontrar um delegado adequado para votar na proposta.

        while(tema != null){

            for (Map.Entry<String, Delegado> entry : delegados.entrySet()) {

                if(entry.getKey().equals(tema.getNome())){
                    return entry.getValue();
                }
            }

            tema = tema.getTemaPai();
        }


        return null;
    }

}
