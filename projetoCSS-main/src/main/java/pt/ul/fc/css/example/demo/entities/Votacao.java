package pt.ul.fc.css.example.demo.entities;

import jakarta.persistence.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import org.springframework.lang.NonNull;

@Entity
public class Votacao {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Integer id;

  @NonNull private String titulo;

  private Integer votosFavor;

  private Integer votosContra;

  @ElementCollection(fetch = FetchType.EAGER)
  private List<Integer> cidVotaram;

  @NonNull
  @Temporal(TemporalType.TIMESTAMP)
  private Date dataExp;

  @OneToOne private Tema tema;

  @NonNull private File pdf;

  @NonNull private Boolean aprovado;

  @NonNull private Boolean fechado;

  public Votacao() {}

  public Votacao(@NonNull ProjetoLei projetoLei) {
    this.aprovado = false;
    this.fechado = false;
    this.titulo = projetoLei.getTitulo();
    this.votosFavor = 0;
    this.votosContra = 0;
    this.cidVotaram = new ArrayList<>();
    this.dataExp = new Date(System.currentTimeMillis() + (30L * 24 * 3600000));
    this.tema = projetoLei.getTema();
    this.pdf = projetoLei.getFicheiro();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (obj == null || obj.getClass() != this.getClass()) return false;
    var that = (Votacao) obj;
    return Objects.equals(this.id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  public List<Integer> getCidVotaram() {
    return cidVotaram;
  }

  @NonNull
  public Tema getTema() {
    return tema;
  }

  public Integer getId() {
    return id;
  }

  public void addVoto(int id, boolean valor) {
    if (!cidVotaram.contains(id)) {
      this.cidVotaram.add(id);
      if (valor) {
        this.votosFavor++;
      } else {
        this.votosContra++;
      }
    } else {
      System.out.println("Cidadão já votou");
    }
  }

  @NonNull
  public Boolean getAprovado() {
    return aprovado;
  }

  @NonNull
  public Boolean getFechado() {
    return fechado;
  }

  public Integer getVotosFavor() {
    return votosFavor;
  }

  public Integer getVotosContra() {
    return votosContra;
  }

  @NonNull
  public Date getDataExp() {
    return dataExp;
  }

  @NonNull
  public File getPdf() {
    return pdf;
  }

  @NonNull
  public String getTitulo() {
    return titulo;
  }

  public void fechaVotacao() {
    this.fechado = true;
  }

  public void aprovaVotacao() {
    this.aprovado = true;
  }

  public boolean cidadaoJaVotou(int cidId) {
    return cidVotaram.contains(cidId);
  }

  public void setDataExp(@NonNull Date date) {
    this.dataExp = date;
  }

  public String toString() {
    return "Votacao"
        + ", dataLim="
        + dataExp
        + ", tema="
        + tema
        + ", titulo='"
        + titulo
        + '\''
        + ", Votos a Favor="
        + votosFavor
        + ", Votos Contra="
        + votosContra
        + '}';
  }
}
