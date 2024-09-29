package pt.ul.fc.css.example.demo.Desktop.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import pt.ul.fc.css.example.demo.entities.Votacao;

public class VotacaoService {
  private HttpClient httpClient;
  private static final String API_URL = "http://localhost:8080/api/votacoes";

  public VotacaoService() {
    this.httpClient = HttpClient.newHttpClient();
  }

  public List<Votacao> getVotacoes() throws Exception {
    HttpRequest request = HttpRequest.newBuilder().uri(new URI(API_URL)).GET().build();

    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.readValue(response.body(), new TypeReference<List<Votacao>>() {});
  }
}
