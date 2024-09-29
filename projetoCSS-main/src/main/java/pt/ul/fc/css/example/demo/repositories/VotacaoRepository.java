package pt.ul.fc.css.example.demo.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pt.ul.fc.css.example.demo.entities.Votacao;

@Repository
public interface VotacaoRepository extends JpaRepository<Votacao, Integer> {
  @Query("SELECT a FROM Votacao a WHERE a.fechado= false")
  List<Votacao> allVotacoes();

  @Query("SELECT v FROM Votacao v WHERE v.id= :q")
  Votacao getVotacaoById(@Param("q") Integer q);

  @Query("SELECT v FROM Votacao v WHERE v.titulo= :q")
  Votacao getVotacaoByName(@Param("q") String q);

  @Query("SELECT EXISTS(SELECT t FROM Votacao t WHERE t.titulo = :q)")
  boolean existsByTitle(@Param("q") String q);

  @Query("SELECT v FROM Votacao v WHERE v.dataExp < NOW()")
  List<Votacao> getVotacoesExpiradas();
}
