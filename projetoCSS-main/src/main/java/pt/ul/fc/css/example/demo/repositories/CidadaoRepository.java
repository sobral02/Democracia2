package pt.ul.fc.css.example.demo.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pt.ul.fc.css.example.demo.entities.Cidadao;
import pt.ul.fc.css.example.demo.entities.Delegado;

@Repository
public interface CidadaoRepository extends JpaRepository<Cidadao, Integer> {
  @Query("SELECT a FROM Cidadao a WHERE a.nome = :q")
  Cidadao findByName(@Param("q") String q);

  @Query("SELECT b FROM Cidadao b")
  List<Cidadao> allCidadaos();

  @Query("SELECT d FROM Cidadao d WHERE d.id = :q")
  Cidadao getCidadaoById(@Param("q") Integer q);
}
