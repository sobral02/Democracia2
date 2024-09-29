package pt.ul.fc.css.example.demo.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.ul.fc.css.example.demo.catalogs.ProjetoLeiCatalog;

@Service
public class ApoiarProjetoLeiHandler {

    @Autowired
    private ProjetoLeiCatalog projetoLeiCatalog;

    public boolean addApoio(Integer projetoId, Integer cidId){

        return projetoLeiCatalog.addApoio(projetoId,cidId);

    }


}
