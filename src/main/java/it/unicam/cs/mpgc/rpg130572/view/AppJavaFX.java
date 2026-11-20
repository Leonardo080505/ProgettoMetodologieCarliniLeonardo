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
import java.net.URL;
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
        primaryStage.setTitle(" Network Hacker ");
        mostraSchermataIniziale();
    }

    private void applicaStileCSS(Scene scene) {
        URL cssResource = getClass().getResource("/style.css");
        if (cssResource != null) {
            scene.getStylesheets().add(cssResource.toExternalForm());
        }
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
        titleLabel.getStyleClass().add("title-label");
        VBox topBox = new VBox(titleLabel);
        topBox.setAlignment(Pos.CENTER);
        root.setTop(topBox);

        VBox centerBox = creaCenterBoxIniziale();
        root.setCenter(centerBox);

        stackPane.getChildren().addAll(matrixBg, root);
        Scene scene = new Scene(stackPane, 650, 450);
        applicaStileCSS(scene);

        stagePrincipale.setScene(scene);
        stagePrincipale.show();
    }

    private VBox creaCenterBoxIniziale() {
        Label infoLabel = new Label("Seleziona un'opzione per entrare nella rete:");
        infoLabel.setStyle("-fx-font-size: 14px;");

        Button btnNuovaPartita = new Button("🎮 Nuova Partita");
        btnNuovaPartita.setStyle("-fx-font-size: 14px; -fx-padding: 10 20;");
        btnNuovaPartita.setOnAction(event -> iniziaNuovaPartita());

        Button btnCaricaPartita = new Button("📂 Carica Partita da JSON");
        btnCaricaPartita.setStyle("-fx-font-size: 14px; -fx-padding: 10 20;");
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
            // Obiettivo impostato sul Mainframe Riservato finale (NODO_13)
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
        lblStatoHacker.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        lblNodoAttuale = new Label();
        lblNodoAttuale.setStyle("-fx-font-size: 13px; -fx-background-color: rgba(31, 40, 51, 0.7); -fx-padding: 8px; -fx-border-color: #66fcf1; -fx-border-radius: 4px;");

        VBox topBox = new VBox(8, lblStatoHacker, lblNodoAttuale);
        topBox.setPadding(new Insets(0, 0, 10, 0));
        root.setTop(topBox);

        txtLog = new TextArea();
        txtLog.setEditable(false);
        txtLog.appendText(messaggioIniziale + "\nObiettivo: Violare il Mainframe Riservato (NODO_13)\n--------------------------------------------------\n");

        Label lblVicini = new Label("Spostati / Scansiona:");
        lblVicini.setStyle("-fx-font-weight: bold;");

        boxAdiacenti = new VBox(8);

        btnHack = new Button("⚡ Tenta Hacking");
        btnHack.getStyleClass().add("button-hack");
        btnHack.setOnAction(event -> {
            String esito = gestore.tentaHackingNodoCorrente();
            txtLog.appendText(esito + "\n");
            aggiornaInterfaccia();
            controllaEsitoPartita();
        });

        Button btnSalva = new Button("💾 Salva Partita");
        btnSalva.setOnAction(event -> eseguiSalvataggio());

        VBox leftBox = new VBox(10, lblVicini, boxAdiacenti, btnHack, btnSalva);
        leftBox.setPrefWidth(260);

        root.setLeft(leftBox);
        root.setCenter(txtLog);

        stackPane.getChildren().addAll(matrixBg, root);

        Scene scene = new Scene(stackPane, 780, 520);
        applicaStileCSS(scene);

        aggiornaInterfaccia();

        stagePrincipale.setScene(scene);
        stagePrincipale.show();
    }

    private void aggiornaInterfaccia() {
        Hacker hacker = gestore.getHacker();
        lblStatoHacker.setText("👤 HACKER: " + hacker.getNickname()
                + "  |  ⚡ ENERGIA: " + hacker.getEnergia()
                + "  |  📈 ABILITÀ: " + hacker.getLivelloAbilita());

        NodoRete nodo = hacker.getNodoCorrente();
        if (nodo != null) {
            String statoViolazione = nodo.isHackerato() ? "✅ VIOLATO" : "❌ NON VIOLATO";
            lblNodoAttuale.setText("📍 POSIZIONE ATTUALE: [" + nodo.getId() + "] " + nodo.getNome()
                    + "\n🔒 Abilità richiesta: " + nodo.getLivelloSicurezza()
                    + "  |  Tipo: " + nodo.getTipo()
                    + "  |  Stato: " + statoViolazione);
            btnHack.setText("⚡ Tenta Hacking (Richiede Abilità: " + nodo.getLivelloSicurezza() + ")");
        }

        boxAdiacenti.getChildren().clear();
        if (nodo != null) {
            List<NodoRete> vicini = mappa.getNodiAdiacenti(nodo.getId());
            for (NodoRete v : vicini) {
                HBox rigaNodo = new HBox(5);

                Button btnSposta = new Button("➡️ " + v.getId() + " (" + v.getTipo() + " - Sicurez: " + v.getLivelloSicurezza() + ")");
                btnSposta.setMaxWidth(Double.MAX_VALUE);
                HBox.setHgrow(btnSposta, Priority.ALWAYS);
                btnSposta.setOnAction(event -> {
                    if (gestore.spostaHacker(v.getId())) {
                        txtLog.appendText("[SPOSTAMENTO]: Spostato nel nodo " + v.getId() + "\n");
                        aggiornaInterfaccia();
                    }
                });

                Button btnScansiona = new Button("🔍");
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
            txtLog.appendText("\n VITTORIA! Hai violato il Mainframe Riservato (NODO_13)!\n");
            Alert alert = new Alert(Alert.AlertType.INFORMATION, " VITTORIA! Hai completato l'obiettivo violando NODO_13!", ButtonType.OK);
            alert.showAndWait();
        } else if (gestore.isSconfitta()) {
            txtLog.appendText("\n☠ SCONFITTA! L'energia dell'Hacker si è azzerata.\n");
            Alert alert = new Alert(Alert.AlertType.ERROR, " SCONFITTA! L'energia dell'Hacker si è azzerata.", ButtonType.OK);
            alert.showAndWait();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}