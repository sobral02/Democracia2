package pt.ul.fc.css.example.demo.entities;

import jakarta.persistence.*;
import java.util.HashMap;
import java.util.Map;
import org.springframework.lang.NonNull;

@Entity
public class Delegado extends Cidadao {

  @ManyToMany(fetch = FetchType.EAGER)
  @MapKeyColumn(name = "id")
  private Map<Cidadao, Tema> cidadaos; // Integer é o id do tema

  public Delegado(@NonNull String nome) {
    super(nome);
    this.cidadaos = new HashMap<>();
  }

  protected Delegado() {
    super();
  }

  public Map<Cidadao, Tema> getCidadaos() {
    return cidadaos;
  }

  public void addCidadao(Cidadao cid, Tema tema) {
    this.cidadaos.put(cid, tema);
  }
}
