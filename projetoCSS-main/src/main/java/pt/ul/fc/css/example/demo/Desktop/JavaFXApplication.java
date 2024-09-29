package pt.ul.fc.css.example.demo.Desktop;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import pt.ul.fc.css.example.demo.Desktop.services.CidadaoService;
import pt.ul.fc.css.example.demo.Desktop.services.ProjetoLeiService;
import pt.ul.fc.css.example.demo.Desktop.services.VotacaoService;
import pt.ul.fc.css.example.demo.entities.*;

public class JavaFXApplication extends Application {

  private ProjetoLeiService projetoLeiService;
  private VotacaoService votacaoService;

  private CidadaoService cidadaoService;

  private HBox hboxApoio; // usado para colocar o botão de apoiar e votar no meio do ecra

  @Override
  public void init() {
    this.projetoLeiService = new ProjetoLeiService();
    this.votacaoService = new VotacaoService();
    this.cidadaoService = new CidadaoService();
  }

  @Override
  public void start(Stage primaryStage) {
    primaryStage.setTitle("Autenticação");

    VBox form = new VBox(10);
    form.setAlignment(Pos.CENTER);

    TextField idField = new TextField();
    idField.setPromptText("Digite o seu Id");
    idField.setStyle("-fx-alignment: center; -fx-max-width: 200; -fx-max-height: 75;");

    Button advanceButton = new Button("Avançar");
    advanceButton.getStyleClass().addAll("rounded-button");
    advanceButton.setOnAction(
        event -> {
          String id = idField.getText();
          int idInt = Integer.parseInt(id);

          if (!id.isEmpty()) {
            try {
              int size = cidadaoService.getCidadaosSize();
              if (size < idInt) {
                // Mensagem erro id invalido
                Label label = new Label("Id inválido");
                form.getChildren().add(label);
              } else {
                boolean isDelegado = cidadaoService.existeDelegadoById(idInt);
                Cidadao cidCorrente;
                if (isDelegado) {
                  cidCorrente = cidadaoService.getDelegadoById(idInt);
                } else {
                  cidCorrente = cidadaoService.getCidadaoById(idInt);
                  // boa ideia enviar o cid corrente p fazer operaçoes do genero
                  // apoiarProjetoLei(cidCorrente.getId())
                }
                showMainMenu(primaryStage, cidCorrente, isDelegado);
              }
            } catch (Exception e) {
              throw new RuntimeException(e);
            }
          }
        });

    form.getChildren().addAll(idField, advanceButton);
    Scene scene = new Scene(form, 800, 600);
    scene
        .getStylesheets()
        .add(Objects.requireNonNull(getClass().getResource("/style.css")).toExternalForm());
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  private void showMainMenu(Stage primaryStage, Cidadao cidcorrente, boolean isDelegado) {
    primaryStage.setTitle("Democracia 2");
    BorderPane root = new BorderPane();
    VBox vbox = new VBox();
    HBox hbox1 = new HBox(); // linha que contem o Nome do user
    HBox hbox = new HBox(); // Adiciona um HBox
    Label nome = new Label("Bem vindo, " + cidcorrente.getNome());
    nome.getStyleClass().add("welcome-label");
    hbox1.getChildren().add(nome);
    Button btn1 = new Button("Listar projetos de lei");
    btn1.getStyleClass().addAll("rounded-button");
    Button btn2 = new Button("Listar votações em curso");
    btn2.getStyleClass().addAll("rounded-button");
    hbox.setMargin(
        btn1, new Insets(0, 0, 0, 10)); // Adiciona um espaço de 10 pixels entre os botões
    hbox.setSpacing(10);
    vbox.setSpacing(10);

    btn1.setOnAction(event -> btn1Event(vbox, cidcorrente));

    btn2.setOnAction(event -> btn2Event(vbox, cidcorrente));

    hbox.getChildren().add(btn1);
    hbox.getChildren().add(btn2);
    if (isDelegado) {
      Button btn3 = new Button("Apresentar projeto lei");
      btn3.getStyleClass().addAll("rounded-button");
      hbox.getChildren().add(btn3);
      btn3.setOnAction(event -> btn3Event(vbox));
    }
    Button btn4 = new Button("Escolher Delegado");
    btn4.getStyleClass().addAll("rounded-button");
    hbox.getChildren().add(btn4);
    btn4.setOnAction(event -> btn3Event(vbox));

    // Adicione o HBox à VBox
    vbox.getChildren().add(hbox1);
    vbox.getChildren().add(hbox);

    root.setCenter(vbox);

    Scene scene = new Scene(root, 800, 600);
    scene
        .getStylesheets()
        .add(Objects.requireNonNull(getClass().getResource("/style.css")).toExternalForm());
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  private void btn3Event(VBox vbox) {
    Label label = new Label("Funcionalidade não implementada.");
    vbox.getChildren()
        .removeIf(
            node ->
                node instanceof TableView
                    || node instanceof Label
                    || node instanceof Button
                    || node == hboxApoio);
    vbox.getChildren().add(label);
  }

  private void btn2Event(VBox vbox, Cidadao cidcorrente) {
    try {

      TableView<Votacao> tableView = new TableView<>();
      tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

      List<Votacao> votacoes = votacaoService.getVotacoes();
      if (votacoes.size() > 0) {

        // Define as colunas para o tableview
        TableColumn<Votacao, String> dataLimCol = new TableColumn<>("Data Lim");
        dataLimCol.setCellValueFactory(new PropertyValueFactory<>("dataExp"));
        dataLimCol.setCellFactory(centerCell());

        TableColumn<Votacao, Tema> temaCol = new TableColumn<>("Tema");
        temaCol.setCellValueFactory(new PropertyValueFactory<>("tema"));
        temaCol.setCellFactory(centerCell());

        TableColumn<Votacao, String> tituloCol = new TableColumn<>("Titulo");
        tituloCol.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        tituloCol.setCellFactory(centerCell());

        TableColumn<Votacao, Integer> votosFavorCol = new TableColumn<>("Votos a favor");
        votosFavorCol.setCellValueFactory(new PropertyValueFactory<>("votosFavor"));
        votosFavorCol.setCellFactory(centerCell());

        TableColumn<Votacao, Integer> votosContraCol = new TableColumn<>("Votos contra");
        votosContraCol.setCellValueFactory(new PropertyValueFactory<>("votosContra"));
        votosContraCol.setCellFactory(centerCell());

        TableColumn<Votacao, String> ficheiroCol = new TableColumn<>("Ficheiro");
        ficheiroCol.setCellValueFactory(new PropertyValueFactory<>("pdf"));
        ficheiroCol.setCellFactory(centerCell());

        TableColumn<Votacao, Boolean> aprovadoCol = new TableColumn<>("Aprovado");
        aprovadoCol.setCellValueFactory(new PropertyValueFactory<>("aprovado"));
        aprovadoCol.setCellFactory(centerCell());

        TableColumn<Votacao, Boolean> fechadoCol = new TableColumn<>("Fechado");
        fechadoCol.setCellValueFactory(new PropertyValueFactory<>("fechado"));
        fechadoCol.setCellFactory(centerCell());
        Button votoButton = new Button("Votar");
        votoButton.getStyleClass().addAll("votar-button");
        votoButton.setOnAction(
            event2 -> {
              String valorVoto = "";
              Votacao selectedVotacao = tableView.getSelectionModel().getSelectedItem();
              if (selectedVotacao != null) {
                boolean result = false;
                int idDelegadoDoTema =
                    cidadaoService.verificarDelegadoVoto(selectedVotacao, cidcorrente);
                if (idDelegadoDoTema == -1) {
                  Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                  alert.setTitle("Opções de Voto");
                  alert.setHeaderText(null);
                  alert.setContentText("Vote a favor ou contra");

                  ButtonType favorablyButton = new ButtonType("Favor");
                  ButtonType nonFavorablyButton = new ButtonType("Contra");
                  ButtonType cancelButton =
                      new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
                  alert.getButtonTypes().setAll(favorablyButton, nonFavorablyButton, cancelButton);

                  Optional<ButtonType> favorResult = alert.showAndWait();

                  if (favorResult.isPresent()) {
                    if (favorResult.get() == favorablyButton) {
                      valorVoto = "a favor";
                      result =
                          cidadaoService.cidadaoVotar(
                              selectedVotacao.getId(), cidcorrente.getId(), true);
                    } else if (favorResult.get() == nonFavorablyButton) {
                      valorVoto = "contra";
                      result =
                          cidadaoService.cidadaoVotar(
                              selectedVotacao.getId(), cidcorrente.getId(), false);
                    } else {
                      return;
                    }
                  }
                } else {
                  // Existe voto por omissao
                  VotoPublico votoDelegado =
                      cidadaoService.getVotoPublicoByDelegadoAndVotId(
                          idDelegadoDoTema, selectedVotacao.getId());
                  Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                  alert.setContentText(
                      "Já possui voto por omissão, este foi apresentado por "
                          + votoDelegado.getDelegado().getNome()
                          + "\ne o seu valor é "
                          + (votoDelegado.getValor() ? "a favor" : "contra")
                          + ". Pode concordar ou discordar já, ou cancelar");
                  ButtonType favorablyButton = new ButtonType("Concordar");
                  ButtonType nonFavorablyButton = new ButtonType("Discordar");
                  ButtonType cancelButton =
                      new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
                  alert.getButtonTypes().setAll(favorablyButton, nonFavorablyButton, cancelButton);
                  Optional<ButtonType> favorResult = alert.showAndWait();
                  if (favorResult.isPresent()) {
                    if (favorResult.get() == favorablyButton) {
                      valorVoto = votoDelegado.getValor() ? "a favor" : "contra";
                      result =
                          cidadaoService.cidadaoVotar(
                              selectedVotacao.getId(),
                              cidcorrente.getId(),
                              votoDelegado.getValor());
                    } else if (favorResult.get() == nonFavorablyButton) {
                      valorVoto = !votoDelegado.getValor() ? "a favor" : "contra";
                      result =
                          cidadaoService.cidadaoVotar(
                              selectedVotacao.getId(),
                              cidcorrente.getId(),
                              !votoDelegado.getValor());
                    } else {
                      return;
                    }
                  }
                }
                if (!result) {
                  Alert alert = new Alert(Alert.AlertType.ERROR);
                  alert.setTitle("Erro Voto");
                  alert.setHeaderText(null);
                  alert.setContentText("Já votou nesta Votacao");
                  alert.showAndWait();
                } else {

                  Alert alert = new Alert(Alert.AlertType.INFORMATION);
                  alert.setTitle("Voce votou");
                  alert.setHeaderText(null);
                  alert.setContentText(
                      "Parabéns! Você votou "
                          + valorVoto
                          + " na Votação "
                          + selectedVotacao.getTitulo()
                          + "!");
                  alert.showAndWait();
                  btn2Event(vbox, cidcorrente);
                }
              } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erro de Seleção");
                alert.setHeaderText(null);
                alert.setContentText("Não selecionou uma Votacao");
                alert.showAndWait();
              }
            });
        tableView
            .getColumns()
            .addAll(
                tituloCol,
                temaCol,
                dataLimCol,
                ficheiroCol,
                votosFavorCol,
                votosContraCol,
                aprovadoCol,
                fechadoCol);
        tableView.getItems().addAll(votacoes);
        clearVBox(vbox);
        hboxApoio = new HBox();
        hboxApoio.setAlignment(Pos.CENTER);
        hboxApoio.getChildren().add(votoButton);
        vbox.getChildren().addAll(tableView, hboxApoio);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void btn1Event(VBox vbox, Cidadao cidcorrente) {
    try {

      TableView<ProjetoLei> tableView = new TableView<>();
      tableView.setColumnResizePolicy(
          TableView.CONSTRAINED_RESIZE_POLICY); // usamos isto para a coluna ocupar
      // todo o espeaço na horizontal

      List<ProjetoLei> projetos = projetoLeiService.getAllProjetosLei();
      if (projetos.size() > 0) {

        // Define as colunas para o tableview
        TableColumn<ProjetoLei, String> dataLimCol = new TableColumn<>("Data Lim");
        dataLimCol.setCellValueFactory(new PropertyValueFactory<>("dataLim"));
        dataLimCol.setCellFactory(centerCell());

        TableColumn<ProjetoLei, Tema> temaCol = new TableColumn<>("Tema");
        temaCol.setCellValueFactory(new PropertyValueFactory<>("tema"));
        temaCol.setCellFactory(centerCell());

        TableColumn<ProjetoLei, String> tituloCol = new TableColumn<>("Titulo");
        tituloCol.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        tituloCol.setCellFactory(centerCell());

        TableColumn<ProjetoLei, String> descricaoCol = new TableColumn<>("Descricao");
        descricaoCol.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        descricaoCol.setCellFactory(centerCell());

        TableColumn<ProjetoLei, String> ficheiroCol = new TableColumn<>("Ficheiro");
        ficheiroCol.setCellValueFactory(new PropertyValueFactory<>("ficheiro"));
        ficheiroCol.setCellFactory(centerCell());

        TableColumn<ProjetoLei, Integer> apoiosCol = new TableColumn<>("Numero de Apoios");
        apoiosCol.setCellValueFactory(new PropertyValueFactory<>("nrApoios"));
        apoiosCol.setCellFactory(centerCell());

        tableView
            .getColumns()
            .addAll(tituloCol, temaCol, descricaoCol, dataLimCol, ficheiroCol, apoiosCol);
        tableView.getItems().addAll(projetos);
      }
      Button apoioButton = new Button("Apoiar");
      apoioButton.getStyleClass().addAll("votar-button");
      apoioButton.setOnAction(
          event2 -> {
            ProjetoLei selectedProjetoLei = tableView.getSelectionModel().getSelectedItem();
            if (selectedProjetoLei != null) {
              boolean result =
                  cidadaoService.cidadaoApoioProjetoLei(
                      selectedProjetoLei.getId(), cidcorrente.getId());
              if (!result) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erro Apoio");
                alert.setHeaderText(null);
                alert.setContentText("Já apoiou este projeto lei.");
                alert.showAndWait();
              } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Voce Apoiou");
                alert.setHeaderText(null);
                alert.setContentText(
                    "Parabéns! Você apoiou no Projeto Lei " + selectedProjetoLei.getTitulo() + "!");
                alert.showAndWait();
                btn1Event(vbox, cidcorrente);
              }
            } else {
              Alert alert = new Alert(Alert.AlertType.ERROR);
              alert.setTitle("Erro Seleção");
              alert.setHeaderText(null);
              alert.setContentText("Não selecionou um Projeto Lei");
              alert.showAndWait();
            }
          });

      clearVBox(vbox);
      hboxApoio = new HBox();
      hboxApoio.setAlignment(Pos.CENTER);
      hboxApoio.getChildren().add(apoioButton);
      vbox.getChildren().addAll(tableView, hboxApoio);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void clearVBox(VBox vbox) {
    vbox.getChildren()
        .removeIf(
            node ->
                node instanceof TableView
                    || node instanceof Label
                    || node instanceof Button
                    || node == hboxApoio);
  }

  public static void main(String[] args) {
    launch(args);
  }

  private <T, S> Callback<TableColumn<S, T>, TableCell<S, T>> centerCell() {
    return column -> {
      TableCell<S, T> cell =
          new TableCell<S, T>() {
            @Override
            protected void updateItem(T item, boolean empty) {
              super.updateItem(item, empty);
              if (empty) {
                setText(null);
              } else {
                setText(item.toString());
              }
            }
          };
      cell.setAlignment(Pos.CENTER);
      return cell;
    };
  }
}
