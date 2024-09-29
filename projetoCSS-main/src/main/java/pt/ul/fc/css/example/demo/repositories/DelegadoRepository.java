package pt.ul.fc.css.example.demo.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pt.ul.fc.css.example.demo.entities.Delegado;

@Repository
public interface DelegadoRepository extends JpaRepository<Delegado, Integer> {

  @Query("SELECT a FROM Delegado a WHERE a.nome LIKE %:q%")
  List<Delegado> findByName(@Param("q") String q);

  @Query("SELECT b FROM Delegado b")
  List<Delegado> allDelegados();

  @Query("SELECT d FROM Delegado d WHERE d.id = :q")
  Delegado getDelegadoById(@Param("q") Integer q);

  @Query("SELECT d FROM Delegado d WHERE d.nome= :q")
  Delegado getDelegadoByName(@Param("q") String q);

  @Query("SELECT EXISTS(SELECT c FROM Delegado c WHERE c.id = :q)")
  boolean isDelegado(@Param("q") Integer q);
}
