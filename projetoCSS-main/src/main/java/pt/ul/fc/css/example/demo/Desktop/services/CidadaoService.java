package pt.ul.fc.css.example.demo.Desktop.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import pt.ul.fc.css.example.demo.entities.*;

public class CidadaoService {
  private HttpClient httpClient;
  private static final String API_URL = "http://localhost:8080/api";

  public CidadaoService() {
    this.httpClient = HttpClient.newHttpClient();
  }

  public boolean cidadaoApoioProjetoLei(int projLeiId, int cidId) {
    String endpoint = API_URL + "/ProjetoLei/" + projLeiId + "/" + cidId;
    try {
      HttpRequest request =
          HttpRequest.newBuilder()
              .uri(new URI(endpoint))
              .PUT(HttpRequest.BodyPublishers.noBody())
              .build();

      HttpResponse<String> response =
          this.httpClient.send(request, HttpResponse.BodyHandlers.ofString());
      return Boolean.parseBoolean(response.body());
    } catch (URISyntaxException e) {
      e.printStackTrace();
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
    return false;
  }

  public Integer postCidadao(String type, String nome) {
    try {
      String endpoint;

      if ("delegado".equalsIgnoreCase(type)) {
        endpoint = API_URL + "/delegado";
      } else {
        endpoint = API_URL + "/cidadao";
      }

      HttpRequest request =
          HttpRequest.newBuilder()
              .uri(new URI(endpoint))
              .POST(HttpRequest.BodyPublishers.ofString(nome))
              .build();

      HttpResponse<String> response =
          httpClient.send(request, HttpResponse.BodyHandlers.ofString());

      ObjectMapper objectMapper = new ObjectMapper();
      return objectMapper.readValue(response.body(), Integer.class);
    } catch (URISyntaxException | IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  public Boolean existeDelegadoById(Integer id) {
    try {
      String endpoint = API_URL + "/delegadoExiste/" + id;

      HttpRequest request = HttpRequest.newBuilder().uri(new URI(endpoint)).GET().build();

      HttpResponse<String> response =
          httpClient.send(request, HttpResponse.BodyHandlers.ofString());

      ObjectMapper objectMapper = new ObjectMapper();
      return objectMapper.readValue(response.body(), Boolean.class);
    } catch (URISyntaxException | IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  public Cidadao getCidadaoById(int parseInt) {
    String endpoint = API_URL;
    endpoint += "/cidadao/" + parseInt;
    return getCidadaoCorrente(endpoint);
  }

  public Cidadao getCidadaoCorrente(String url) {
    try {
      HttpRequest request = HttpRequest.newBuilder().uri(new URI(url)).GET().build();

      HttpResponse<String> response =
          httpClient.send(request, HttpResponse.BodyHandlers.ofString());

      ObjectMapper objectMapper = new ObjectMapper();
      return objectMapper.readValue(response.body(), Cidadao.class);
    } catch (URISyntaxException | IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  public Delegado getDelegadoCorrente(String url) {
    try {
      HttpRequest request = HttpRequest.newBuilder().uri(new URI(url)).GET().build();

      HttpResponse<String> response =
          httpClient.send(request, HttpResponse.BodyHandlers.ofString());

      ObjectMapper objectMapper = new ObjectMapper();
      return objectMapper.readValue(response.body(), Delegado.class);
    } catch (URISyntaxException | IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  public int getCidadaosSize() {
    try {
      HttpRequest request =
          HttpRequest.newBuilder().uri(new URI(API_URL + "/cidadaosSize")).GET().build();

      HttpResponse<String> response =
          httpClient.send(request, HttpResponse.BodyHandlers.ofString());

      ObjectMapper objectMapper = new ObjectMapper();
      return objectMapper.readValue(response.body(), Integer.class);
    } catch (URISyntaxException | IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  public Delegado getDelegadoById(int idInt) {
    String endpoint = API_URL;
    endpoint += "/delegado/" + idInt;
    return getDelegadoCorrente(endpoint);
  }

  public boolean cidadaoVotar(Integer votId, Integer cidId, boolean votoValor) {
    String url = API_URL + "/votacao/" + votId + "/" + cidId;

    try {
      HttpRequest request =
          HttpRequest.newBuilder()
              .uri(new URI(url))
              .header("Content-Type", "application/json")
              .PUT(HttpRequest.BodyPublishers.ofString(String.valueOf(votoValor)))
              .build();

      HttpResponse<String> response =
          httpClient.send(request, HttpResponse.BodyHandlers.ofString());
      if (response.statusCode() == 200) {
        return Boolean.parseBoolean(response.body());
      } else {
        throw new RuntimeException(
            "HTTP PUT request failed with status code: " + response.statusCode());
      }
    } catch (Exception e) {
      throw new RuntimeException("Error sending PUT request: " + e.getMessage());
    }
  }

  public Integer verificarDelegadoVoto(Votacao selectedVotacao, Cidadao cidcorrente) {
    Tema currentTema = selectedVotacao.getTema();
    while (currentTema != null) {
      if (cidcorrente.getDelegados().containsKey(currentTema.getNome())) {
        int idDel = cidcorrente.getDelegados().get(selectedVotacao.getTema().getNome()).getId();
        if (selectedVotacao.cidadaoJaVotou(idDel)) {
          return idDel;
        } else {
          return -1;
        }
      }
      currentTema = currentTema.getTemaPai();
    }
    return -1;
  }

  public VotoPublico getVotoPublicoByDelegadoAndVotId(int idDelegadoDoTema, Integer votId) {
    String endpoint = API_URL;
    endpoint += "/VotosPublicos/delegado/" + idDelegadoDoTema + "/votacao/" + votId;
    try {
      HttpRequest request = HttpRequest.newBuilder().uri(new URI(endpoint)).GET().build();

      HttpResponse<String> response =
          httpClient.send(request, HttpResponse.BodyHandlers.ofString());

      ObjectMapper objectMapper = new ObjectMapper();
      return objectMapper.readValue(response.body(), VotoPublico.class);
    } catch (URISyntaxException | IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
