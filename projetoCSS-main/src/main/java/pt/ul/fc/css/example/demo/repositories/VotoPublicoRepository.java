package pt.ul.fc.css.example.demo.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pt.ul.fc.css.example.demo.entities.VotoPublico;

@Repository
public interface VotoPublicoRepository extends JpaRepository<VotoPublico, Integer> {
  @Query("SELECT vp FROM VotoPublico vp WHERE vp.delegado.id =:delId")
  List<VotoPublico> getVotoPublicoByDelegado_Id(@Param("delId") Integer delId);

  @Query("SELECT vp FROM VotoPublico vp")
  List<VotoPublico> allVotosPublicos();

  @Query("SELECT vp FROM VotoPublico vp WHERE vp.votacao.titulo = :titulo")
  List<VotoPublico> getVotosPublicoByTitulo(@Param("titulo") String titulo);

  @Query("SELECT vp FROM VotoPublico vp WHERE vp.delegado.id =:delId AND vp.votacao.id=:votId")
  VotoPublico getVotoPublicoByDelegadoAndVotId(
      @Param("delId") Integer delId, @Param("votId") Integer votId);
}
