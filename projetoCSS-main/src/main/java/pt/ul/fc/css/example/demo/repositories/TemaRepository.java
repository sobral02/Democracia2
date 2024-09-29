package pt.ul.fc.css.example.demo.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pt.ul.fc.css.example.demo.entities.Tema;

@Repository
public interface TemaRepository extends JpaRepository<Tema, Integer> {

  @Query("SELECT t FROM Tema t")
  List<Tema> allTemas();

  @Query("SELECT t FROM Tema t WHERE t.id = :q")
  Tema getTemaById(@Param("q") Integer q);

  @Query("SELECT t FROM Tema t WHERE t.nome = :q")
  Tema getByNome(@Param("q") String q);

  @Query("SELECT EXISTS(SELECT t FROM Tema t WHERE t.nome = :q)")
  boolean existsByTitle(@Param("q") String q);
}
