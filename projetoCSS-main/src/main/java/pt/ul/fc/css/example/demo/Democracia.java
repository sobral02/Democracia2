package pt.ul.fc.css.example.demo;

import java.util.Calendar;
import java.util.Date;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import pt.ul.fc.css.example.demo.catalogs.*;
import pt.ul.fc.css.example.demo.handlers.EscolherDelegadoHandler;

@SpringBootApplication
@EnableScheduling
@EnableJpaRepositories
public class Democracia {

  public static void main(String[] args) {
    SpringApplication.run(Democracia.class, args);
  }

  @Bean
  public CommandLineRunner demo(
      CidadaoCatalog cidadaoCatalog,
      DelegadoCatalog delegadoCatalog,
      VotacaoCatalog votacaoCatalog,
      ProjetoLeiCatalog projetoLeiCatalog,
      TemaCatalog temaCatalog,
      EscolherDelegadoHandler handler) {
    return (args) -> {
      Calendar calendar = Calendar.getInstance();
      calendar.add(Calendar.WEEK_OF_MONTH, 10);
      Date twoWeeksLater = calendar.getTime();

      int id1 =
          projetoLeiCatalog.addProjetoLei(
              delegadoCatalog.addDelegado("Joao"),
              temaCatalog.getTemaByName("EDUCACAO"),
              "SECA",
              "descricao nada",
              "src/main/resources/teste.pdf",
              twoWeeksLater);

      votacaoCatalog.addVotacao(projetoLeiCatalog.getProjetoLeiById(id1));
      int idDel = delegadoCatalog.addDelegado("Luis");
      int idCid = cidadaoCatalog.addCidadao("Tiago");
      handler.EscolherDelegado(idCid, idDel, "EDUCACAO");
      /*
      // save a few customers'
      repository.save(new Author("Jack", "Bauer"));
      repository.save(new Author("Chloe", "O'Brian"));
      repository.save(new Author("Kim", "Bauer"));
      repository.save(new Author("David", "Palmer"));
      repository.save(new Author("Michelle", "Dessler"));

      // fetch all customers
      log.info("Customers found with findAll():");
      log.info("-------------------------------");
      for (Author author : repository.findAll()) {
          log.info(author.toString());
      }
      log.info("");

      // fetch an individual customer by ID
      repository.findById(1L).ifPresent((Author author) -> {
          log.info("Customer found with findById(1L):");
          log.info("--------------------------------");
          log.info(author.toString());
          log.info("");
      });

      // fetch customers by last name
      log.info("Author found with findByName('Bauer'):");
      log.info("--------------------------------------------");
      repository.findByName("Bauer").forEach(bauer -> {
          log.info(bauer.toString());
      });
      // for (Customer bauer : repository.findByLastName("Bauer")) {
      // log.info(bauer.toString());
      // }
      log.info("");

       */
    };
  }
}
