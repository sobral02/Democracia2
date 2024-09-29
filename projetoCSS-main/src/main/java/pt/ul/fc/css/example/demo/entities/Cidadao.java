package pt.ul.fc.css.example.demo.entities;

import jakarta.persistence.*;
import java.util.HashMap;
import java.util.Map;
import org.springframework.lang.NonNull;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Cidadao {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Integer id;

  @NonNull private String nome;
  @ManyToMany(fetch = FetchType.EAGER)
  @MapKeyColumn(name = "id")
  private final Map<String,Delegado> delegados = new HashMap<>();

  public Cidadao() {}

  public Cidadao(@NonNull String nome) {
    this.nome = nome;
  }

  @NonNull
  public String getNome() {
    return nome;
  }

  public Integer getId() {
    return id;
  }

  public Map<String, Delegado> getDelegados() {
    return this.delegados;
  }

  public void addDelegado(Tema tema, Delegado delegado) {
    delegados.put(tema.getNome(),delegado);
  }

  @Override
  public String toString() {
    return "Cidadao:"+nome;
  }
}
