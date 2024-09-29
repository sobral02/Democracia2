package pt.ul.fc.css.example.demo.entities;

import jakarta.persistence.*;
import java.util.Objects;
import org.springframework.lang.NonNull;

@Entity
public class Tema {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Integer id;

  @NonNull private String nome;

  @ManyToOne
  @JoinColumn(name = "tema_pai_id") // verificar necessidade desta anotação
  private Tema temaPai;

  public Tema() {}

  public Tema(@NonNull String nome, Tema temaPai) {
    this.nome = nome;
    this.temaPai = temaPai;
  }

  public Integer getId() {
    return id;
  }

  @NonNull
  public String getNome() {
    return nome;
  }

  public Tema getTemaPai() {
    return temaPai;
  }

  public void setTemaPai(Tema temaPai) {
    this.temaPai = temaPai;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Tema)) return false;
    return Objects.equals(id, ((Tema) o).id);
  }

  @Override
  public String toString() {
    return getNome();
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
