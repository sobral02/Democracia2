package pt.ul.fc.css.example.demo.entities;

import jakarta.persistence.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import org.springframework.lang.NonNull;
import pt.ul.fc.css.example.demo.handlers.FileHandler;

@Entity
public class ProjetoLei {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Integer id;

  @NonNull
  @Temporal(TemporalType.TIMESTAMP)
  private Date dataLim;

  @NonNull @ManyToOne private Tema tema;

  @NonNull private String titulo;

  @NonNull public String descricao;

  public File ficheiro;

  private Integer nrApoios;

  @Override
  public String toString() {
    return "ProjetoLei"
        + ", dataLim="
        + dataLim
        + ", tema="
        + tema
        + ", titulo='"
        + titulo
        + '\''
        + ", descricao='"
        + descricao
        + '\''
        + ", ficheiro="
        + ficheiro
        + ", apoios="
        + apoios
        + '}';
  }

  @ElementCollection(fetch = FetchType.EAGER)
  private List<Integer> apoios;

  public ProjetoLei(
      @NonNull Tema tema,
      @NonNull String titulo,
      @NonNull String descricao,
      @NonNull String ficheiro,
      @NonNull Date dataLim) {
    this.dataLim = dataLim;
    this.tema = tema;
    this.titulo = titulo;
    this.descricao = descricao;
    this.nrApoios = 0;
    this.ficheiro = FileHandler.fileToBinaryFile(ficheiro, "ProjLei_" + titulo);
    this.apoios = new ArrayList<>();
  }

  public ProjetoLei() {}

  public Tema getTema() {
    return tema;
  }

  public List<Integer> getApoios() {
    return apoios;
  }

  public String getDescricao() {
    return descricao;
  }

  public File getFicheiro() {
    return ficheiro;
  }

  public Date getDataLim() {
    return dataLim;
  }

  public String getTitulo() {
    return titulo;
  }

  public Integer getId() {
    return id;
  }

  public boolean addApoios(Integer id) {
    if (!apoios.contains(id)) {
      this.apoios.add(id);
      this.nrApoios++;
      return true;
    } else {
      return false;
    }
  }

  public Integer getNrApoios() {
    return nrApoios;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (obj == null || obj.getClass() != this.getClass()) return false;
    var that = (ProjetoLei) obj;
    return Objects.equals(this.id, that.id);
  }
}
