package pt.ul.fc.css.example.demo.entities;

import jakarta.persistence.*;
import org.springframework.lang.NonNull;

@Entity
public class VotoPublico {

  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Id
  private Integer id;

  @ManyToOne private Delegado delegado;

  private boolean valor;

  @ManyToOne private Votacao votacao;

  public VotoPublico() {}

  public VotoPublico(Delegado delegado, @NonNull Votacao votacao, boolean valor) {
    this.delegado = delegado;
    this.votacao = votacao;
    this.valor = valor;
  }

  public Votacao getVotacao() {
    return votacao;
  }

  public Integer getId() {
    return id;
  }

  public Delegado getDelegado() {
    return this.delegado;
  }

  public boolean getValor() {
    return this.valor;
  }

  @Override
  public String toString() {
    return "VotoPublico{"
        + "id="
        + id
        + ", delegado="
        + delegado
        + ", valor="
        + valor
        + ", votacao="
        + votacao
        + '}';
  }
}
