package it.unicam.cs.mpgc.rpg130572.view;

import it.unicam.cs.mpgc.rpg130572.controller.GestorePartita;
import it.unicam.cs.mpgc.rpg130572.model.*;
import it.unicam.cs.mpgc.rpg130572.persistence.CaricatoreMappa;
import it.unicam.cs.mpgc.rpg130572.persistence.CaricatoreMappaJson;
import it.unicam.cs.mpgc.rpg130572.persistence.CaricatoreSalvataggioJson;
import it.unicam.cs.mpgc.rpg130572.persistence.GestoreSalvataggioJson;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;


public class AppJavaFX extends Application {

    private Stage stagePrincipale;
    private MappaRete mappa;
    private GestorePartita gestore;

    private Label lblStatoHacker;
    private Label lblNodoAttuale;
    private Button btnHack;
    private TextArea txtLog;
    private VBox boxAdiacenti;

    @Override
    public void start(Stage primaryStage) {
        this.stagePrincipale = primaryStage;
        primaryStage.setTitle("Network Hacker");
        mostraSchermataIniziale();
    }

    private void mostraSchermataIniziale() {
        StackPane stackPane = new StackPane();
        MatrixCanvas matrixBg = new MatrixCanvas(650, 450);
        matrixBg.widthProperty().bind(stackPane.widthProperty());
        matrixBg.heightProperty().bind(stackPane.heightProperty());

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: transparent;");

        Label titleLabel = new Label("=== NETWORK HACKER ===");
        titleLabel.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: #66fcf1; "
                + "-fx-effect: dropshadow(three-pass-box, #66fcf1, 10, 0, 0, 0);");

        VBox topBox = new VBox(titleLabel);
        topBox.setAlignment(Pos.CENTER);
        root.setTop(topBox);

        VBox centerBox = creaCenterBoxIniziale();
        root.setCenter(centerBox);

        stackPane.getChildren().addAll(matrixBg, root);
        Scene scene = new Scene(stackPane, 650, 450);

        stagePrincipale.setScene(scene);
        stagePrincipale.show();
    }

    private VBox creaCenterBoxIniziale() {
        Label infoLabel = new Label("Seleziona un'opzione per entrare nella rete:");
        infoLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #c5c6c7; -fx-font-weight: bold;");

        Button btnNuovaPartita = new Button("Nuova Partita");
        stilaBottone(btnNuovaPartita, "#1f2833", "#66fcf1", "#66fcf1");
        btnNuovaPartita.setStyle(btnNuovaPartita.getStyle() + " -fx-font-size: 14px; -fx-padding: 10 20;");
        btnNuovaPartita.setOnAction(event -> iniziaNuovaPartita());

        Button btnCaricaPartita = new Button("Carica Partita da JSON");
        stilaBottone(btnCaricaPartita, "#1f2833", "#66fcf1", "#66fcf1");
        btnCaricaPartita.setStyle(btnCaricaPartita.getStyle() + " -fx-font-size: 14px; -fx-padding: 10 20;");
        btnCaricaPartita.setOnAction(event -> caricaPartitaEsistente());

        VBox centerBox = new VBox(15, infoLabel, btnNuovaPartita, btnCaricaPartita);
        centerBox.setAlignment(Pos.CENTER);
        return centerBox;
    }

    private void iniziaNuovaPartita() {
        try {
            CaricatoreMappa caricatore = new CaricatoreMappaJson();
            this.mappa = caricatore.caricaDaRisorse("mappa.json");
            Hacker hacker = new Hacker("CyberZero", 100, 2);
            this.gestore = new GestorePartita(hacker, mappa, "NODO_13");
            this.gestore.spostaHacker("NODO_01");

            mostraSchermataGioco("Nuova Partita avviata!");
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Errore durante il caricamento della mappa: " + ex.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }
    }

    private void caricaPartitaEsistente() {
        try {
            File fileSalvataggio = new File("salvataggio.json");
            if (!fileSalvataggio.exists()) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Nessun salvataggio trovato! Avvia prima una nuova partita e salvala.", ButtonType.OK);
                alert.showAndWait();
                return;
            }

            CaricatoreMappa caricatoreMappa = new CaricatoreMappaJson();
            this.mappa = caricatoreMappa.caricaDaRisorse("mappa.json");

            CaricatoreSalvataggioJson caricatoreSalvataggio = new CaricatoreSalvataggioJson();
            this.gestore = caricatoreSalvataggio.caricaPartita("salvataggio.json", mappa);

            mostraSchermataGioco("Partita caricata con successo da salvataggio.json!");
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Errore durante il caricamento del salvataggio: " + ex.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }
    }

    private void mostraSchermataGioco(String messaggioIniziale) {
        StackPane stackPane = new StackPane();
        MatrixCanvas matrixBg = new MatrixCanvas(780, 520);
        matrixBg.widthProperty().bind(stackPane.widthProperty());
        matrixBg.heightProperty().bind(stackPane.heightProperty());

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(15));
        root.setStyle("-fx-background-color: transparent;");

        lblStatoHacker = new Label();
        lblStatoHacker.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #66fcf1;");

        lblNodoAttuale = new Label();
        lblNodoAttuale.setStyle("-fx-font-size: 13px; -fx-text-fill: #c5c6c7; -fx-background-color: rgba(31, 40, 51, 0.85); "
                + "-fx-padding: 10px; -fx-border-color: #66fcf1; -fx-border-radius: 4px;");

        VBox topBox = new VBox(8, lblStatoHacker, lblNodoAttuale);
        topBox.setPadding(new Insets(0, 0, 10, 0));
        root.setTop(topBox);

        txtLog = new TextArea();
        txtLog.setEditable(false);
        txtLog.setStyle("-fx-control-inner-background: #0b0c10; -fx-text-fill: #00ff66; -fx-font-family: 'Consolas', monospace; "
                + "-fx-font-size: 12px; -fx-border-color: #45f3ff; -fx-border-radius: 4px;");
        txtLog.appendText(messaggioIniziale + "\nObiettivo: Violare il Mainframe Riservato (NODO_13)\n--------------------------------------------------\n");

        Label lblVicini = new Label("Spostati / Scansiona:");
        lblVicini.setStyle("-fx-font-weight: bold; -fx-text-fill: #66fcf1; -fx-font-size: 13px;");

        boxAdiacenti = new VBox(8);

        btnHack = new Button("Tenta Hacking");
        stilaBottoneSpeciale(btnHack, "#ff2a6d", "#ffffff", "#ff5c8d");
        btnHack.setOnAction(event -> {
            String esito = gestore.tentaHackingNodoCorrente();
            txtLog.appendText(esito + "\n");
            aggiornaInterfaccia();
            controllaEsitoPartita();
        });

        Button btnSalva = new Button("Salva Partita");
        stilaBottone(btnSalva, "#1f2833", "#66fcf1", "#66fcf1");
        btnSalva.setOnAction(event -> eseguiSalvataggio());

        VBox leftBox = new VBox(10, lblVicini, boxAdiacenti, btnHack, btnSalva);
        leftBox.setPrefWidth(270);
        leftBox.setPadding(new Insets(0, 10, 0, 0));

        root.setLeft(leftBox);
        root.setCenter(txtLog);

        stackPane.getChildren().addAll(matrixBg, root);

        Scene scene = new Scene(stackPane, 780, 520);
        aggiornaInterfaccia();

        stagePrincipale.setScene(scene);
        stagePrincipale.show();
    }

    private void aggiornaInterfaccia() {
        Hacker hacker = gestore.getHacker();
        lblStatoHacker.setText("HACKER: " + hacker.getNickname()
                + "  |  ENERGIA: " + hacker.getEnergia()
                + "  |  ABILITA: " + hacker.getLivelloAbilita());

        NodoRete nodo = hacker.getNodoCorrente();
        if (nodo != null) {
            String statoViolazione = nodo.isHackerato() ? "VIOLATO" : "NON VIOLATO";
            lblNodoAttuale.setText("POSIZIONE ATTUALE: [" + nodo.getId() + "] " + nodo.getNome()
                    + "\nAbilita richiesta: " + nodo.getLivelloSicurezza()
                    + "  |  Tipo: " + nodo.getTipo()
                    + "  |  Stato: " + statoViolazione);

            btnHack.setText("Tenta Hacking (Richiede Abilita: " + nodo.getLivelloSicurezza() + ")");
        }

        boxAdiacenti.getChildren().clear();
        if (nodo != null) {
            List<NodoRete> vicini = mappa.getNodiAdiacenti(nodo.getId());
            for (NodoRete v : vicini) {
                HBox rigaNodo = new HBox(5);

                Button btnSposta = new Button("--> " + v.getId() + " (Sicurez: " + v.getLivelloSicurezza() + ")");
                btnSposta.setMaxWidth(Double.MAX_VALUE);
                HBox.setHgrow(btnSposta, Priority.ALWAYS);
                stilaBottone(btnSposta, "#1f2833", "#c5c6c7", "#45f3ff");

                btnSposta.setOnAction(event -> {
                    if (gestore.spostaHacker(v.getId())) {
                        txtLog.appendText("[SPOSTAMENTO]: Spostato nel nodo " + v.getId() + "\n");
                        aggiornaInterfaccia();
                    }
                });

                Button btnScansiona = new Button("Scansiona");
                stilaBottone(btnScansiona, "#1f2833", "#66fcf1", "#66fcf1");
                btnScansiona.setTooltip(new Tooltip("Scansiona nodo " + v.getId()));

                btnScansiona.setOnAction(event -> {
                    String esitoScansione = gestore.scansionaNodoTarget(v.getId());
                    txtLog.appendText(esitoScansione + "\n");
                    aggiornaInterfaccia();
                    controllaEsitoPartita();
                });

                rigaNodo.getChildren().addAll(btnSposta, btnScansiona);
                boxAdiacenti.getChildren().add(rigaNodo);
            }
        }
    }

    private void stilaBottone(Button btn, String bgColor, String textColor, String borderColor) {
        String baseStyle = String.format("-fx-background-color: %s; -fx-text-fill: %s; -fx-border-color: %s; "
                        + "-fx-border-radius: 4px; -fx-background-radius: 4px; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 6 10;",
                bgColor, textColor, borderColor);

        String hoverStyle = String.format("-fx-background-color: %s; -fx-text-fill: #0b0c10; -fx-border-color: %s; "
                        + "-fx-border-radius: 4px; -fx-background-radius: 4px; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 6 10;",
                borderColor, borderColor);

        btn.setStyle(baseStyle);
        btn.setOnMouseEntered(e -> btn.setStyle(hoverStyle));
        btn.setOnMouseExited(e -> btn.setStyle(baseStyle));
    }

    private void stilaBottoneSpeciale(Button btn, String bgColor, String textColor, String hoverBgColor) {
        String baseStyle = String.format("-fx-background-color: %s; -fx-text-fill: %s; -fx-border-color: %s; "
                        + "-fx-border-radius: 4px; -fx-background-radius: 4px; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 8 12; -fx-cursor: hand;",
                bgColor, textColor, bgColor);

        String hoverStyle = String.format("-fx-background-color: %s; -fx-text-fill: %s; -fx-border-color: %s; "
                        + "-fx-border-radius: 4px; -fx-background-radius: 4px; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 8 12; -fx-cursor: hand;",
                hoverBgColor, textColor, hoverBgColor);

        btn.setStyle(baseStyle);
        btn.setOnMouseEntered(e -> btn.setStyle(hoverStyle));
        btn.setOnMouseExited(e -> btn.setStyle(baseStyle));
    }

    private void eseguiSalvataggio() {
        try {
            GestoreSalvataggioJson salvataggio = new GestoreSalvataggioJson();
            salvataggio.salvaPartita(gestore, "salvataggio.json");
            txtLog.appendText("[SYSTEM]: Partita salvata correttamente in salvataggio.json!\n");
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Errore durante il salvataggio: " + ex.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }
    }

    private void controllaEsitoPartita() {
        if (gestore.isVittoria()) {
            txtLog.appendText("\n[VITTORIA] Hai violato il Mainframe Riservato (NODO_13)!\n");
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "VITTORIA! Hai completato l'obiettivo violando NODO_13!", ButtonType.OK);
            alert.showAndWait();
        } else if (gestore.isSconfitta()) {
            txtLog.appendText("\n[SCONFITTA] L'energia dell'Hacker si e' azzerata.\n");
            Alert alert = new Alert(Alert.AlertType.ERROR, "SCONFITTA! L'energia dell'Hacker si e' azzerata.", ButtonType.OK);
            alert.showAndWait();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}