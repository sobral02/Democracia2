package pt.ul.fc.css.example.demo.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pt.ul.fc.css.example.demo.entities.ProjetoLei;
import pt.ul.fc.css.example.demo.entities.Tema;

@Repository
public interface ProjetoLeiRepository extends JpaRepository<ProjetoLei, Integer> {

  @Query("SELECT EXISTS(SELECT pj FROM ProjetoLei pj WHERE pj.titulo = :q)")
  boolean existsByTitle(@Param("q") String q);

  @Query("SELECT a FROM ProjetoLei a WHERE a.id = :q")
  ProjetoLei getProjetoLeiById(@Param("q") Integer q);

  @Query("SELECT b FROM ProjetoLei b WHERE b.titulo = :q")
  ProjetoLei getByTitle(@Param("q") String q);

  @Query("SELECT c FROM ProjetoLei c")
  List<ProjetoLei> allProjetoLei();

  @Query("SELECT d FROM ProjetoLei d WHERE d.tema = :q")
  List<ProjetoLei> getByTema(@Param("q") Tema q);

  @Query("SELECT d FROM ProjetoLei d WHERE d.dataLim < NOW()")
  List<ProjetoLei> getProjetosLeiExpirados();

  @Query("SELECT d FROM ProjetoLei d WHERE d.tema.nome = :q")
  List<ProjetoLei> getByNomeTema(@Param("q") String q);

  @Query("SELECT e FROM ProjetoLei e WHERE SIZE(e.apoios) >= 10000")
  List<ProjetoLei> getProjetosLeiComAprovacao();
}
