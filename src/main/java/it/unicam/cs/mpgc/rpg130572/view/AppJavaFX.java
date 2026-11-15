package it.unicam.cs.mpgc.rpg130572.view;

import it.unicam.cs.mpgc.rpg130572.controller.GestorePartita;
import it.unicam.cs.mpgc.rpg130572.model.*;
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
        primaryStage.setTitle("Cyberpunk Network Hacker - GUI");

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

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: transparent;");

        Label titleLabel = new Label("=== CYBERPUNK NETWORK HACKER ===");
        titleLabel.getStyleClass().add("title-label");

        VBox topBox = new VBox(titleLabel);
        topBox.setAlignment(Pos.CENTER);
        root.setTop(topBox);

        Label infoLabel = new Label("Seleziona un'opzione per entrare nella rete:");
        infoLabel.setStyle("-fx-font-size: 14px;");

        Button btnNuovaPartita = new Button("🎮 Nuova Partita");
        btnNuovaPartita.setStyle("-fx-font-size: 14px; -fx-padding: 10 20;");
        btnNuovaPartita.setOnAction(e -> iniziaNuovaPartita());

        Button btnCaricaPartita = new Button("📂 Carica Partita da JSON");
        btnCaricaPartita.setStyle("-fx-font-size: 14px; -fx-padding: 10 20;");
        btnCaricaPartita.setOnAction(e -> caricaPartitaEsistente());

        VBox centerBox = new VBox(15, infoLabel, btnNuovaPartita, btnCaricaPartita);
        centerBox.setAlignment(Pos.CENTER);
        root.setCenter(centerBox);

        stackPane.getChildren().addAll(matrixBg, root);

        Scene scene = new Scene(stackPane, 650, 450);
        applicaStileCSS(scene);
        stagePrincipale.setScene(scene);
        stagePrincipale.show();
    }

    private void iniziaNuovaPartita() {
        CaricatoreMappaJson caricatore = new CaricatoreMappaJson();
        this.mappa = caricatore.caricaDaRisorse("mappa.json");
        Hacker hacker = new Hacker("CyberZero", 100, 2);

        this.gestore = new GestorePartita(hacker, mappa, "NODO_07");
        this.gestore.spostaHacker("NODO_01");

        mostraSchermataGioco("Nuova Partita avviata!");
    }

    private void caricaPartitaEsistente() {
        File fileSalvataggio = new File("salvataggio.json");
        if (!fileSalvataggio.exists()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Nessun salvataggio trovato! Avvia prima una nuova partita e salvala.", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        CaricatoreMappaJson caricatoreMappa = new CaricatoreMappaJson();
        this.mappa = caricatoreMappa.caricaDaRisorse("mappa.json");

        CaricatoreSalvataggioJson caricatoreSalvataggio = new CaricatoreSalvataggioJson();
        this.gestore = caricatoreSalvataggio.caricaPartita("salvataggio.json", mappa);

        mostraSchermataGioco("Partita caricata con successo da salvataggio.json!");
    }

    private void mostraSchermataGioco(String messaggioIniziale) {
        StackPane stackPane = new StackPane();
        MatrixCanvas matrixBg = new MatrixCanvas(780, 520);

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
        txtLog.appendText(messaggioIniziale + "\nObiettivo: Violare il Mainframe (NODO_07)\n--------------------------------------------------\n");

        Label lblVicini = new Label("Spostati / Scansiona:");
        lblVicini.setStyle("-fx-font-weight: bold;");

        boxAdiacenti = new VBox(8);

        btnHack = new Button("⚡ Tenta Hacking");
        btnHack.getStyleClass().add("button-hack");
        btnHack.setMaxWidth(Double.MAX_VALUE);
        btnHack.setOnAction(e -> eseguiHacking());

        Button btnSalva = new Button("💾 Salva Partita");
        btnSalva.setMaxWidth(Double.MAX_VALUE);
        btnSalva.setOnAction(e -> eseguiSalvataggio());

        VBox rightBox = new VBox(10, lblVicini, boxAdiacenti, new Separator(), btnHack, btnSalva);
        rightBox.setPadding(new Insets(0, 0, 0, 15));
        rightBox.setPrefWidth(280);

        root.setCenter(txtLog);
        root.setRight(rightBox);

        aggiornaInterfaccia();

        stackPane.getChildren().addAll(matrixBg, root);

        Scene scene = new Scene(stackPane, 780, 520);
        applicaStileCSS(scene);
        stagePrincipale.setScene(scene);
    }

    private void aggiornaInterfaccia() {
        Hacker h = gestore.getHacker();
        lblStatoHacker.setText("HACKER: " + h.getNickname() + " | ⚡ Energia: " + h.getEnergia() + " | 💻 Abilità: " + h.getLivelloAbilita());

        NodoRete nodo = h.getNodoCorrente();
        if (nodo != null) {
            String statoViolazione = nodo.isHackerato() ? "✅ VIOLATO" : "❌ NON VIOLATO";
            lblNodoAttuale.setText("📍 POSIZIONE ATTUALE: [" + nodo.getId() + "] " + nodo.getNome()
                    + "\n🔒 Abilità richiesta per Hacking: " + nodo.getLivelloSicurezza()
                    + "  |  Stato Nodo: " + statoViolazione);

            // Aggiorna la dicitura del pulsante Hacking con il livello di abilità richiesto
            btnHack.setText("⚡ Tenta Hacking (Richiede Abilità: " + nodo.getLivelloSicurezza() + ")");
        }

        boxAdiacenti.getChildren().clear();
        if (nodo != null) {
            List<NodoRete> vicini = mappa.getNodiAdiacenti(nodo.getId());
            for (NodoRete v : vicini) {
                HBox rigaNodo = new HBox(5);

                // Pulsante Spostamento con ID e Livello Sicurezza del nodo adiacente
                Button btnSposta = new Button("➡️ " + v.getId() + " (Sicurez: " + v.getLivelloSicurezza() + ")");
                btnSposta.setMaxWidth(Double.MAX_VALUE);
                HBox.setHgrow(btnSposta, Priority.ALWAYS);
                btnSposta.setOnAction(ev -> {
                    if (gestore.spostaHacker(v.getId())) {
                        txtLog.appendText("[SPOSTAMENTO]: Spostato nel nodo " + v.getId() + "\n");
                        aggiornaInterfaccia();
                    }
                });

                // Pulsante Scansione Anti-Trappola
                Button btnScan = new Button("🔍");
                btnScan.setTooltip(new Tooltip("Scansiona nodo (-5 energia)"));
                btnScan.setOnAction(ev -> {
                    String esitoScan = gestore.scansionaNodo(v.getId());
                    txtLog.appendText("[SCAN]: " + esitoScan + "\n");
                    aggiornaInterfaccia();
                });

                rigaNodo.getChildren().addAll(btnSposta, btnScan);
                boxAdiacenti.getChildren().add(rigaNodo);
            }
        }
    }

    private void eseguiHacking() {
        String esito = gestore.tentaHackingNodoCorrente();
        txtLog.appendText("[HACKING]: " + esito + "\n");
        aggiornaInterfaccia();
        controllaEsitoPartita();
    }

    private void eseguiSalvataggio() {
        GestoreSalvataggioJson salvataggio = new GestoreSalvataggioJson();
        salvataggio.salvaPartita(gestore, "salvataggio.json");
        txtLog.appendText("[SYSTEM]: Partita salvata correttamente in salvataggio.json!\n");
    }

    private void controllaEsitoPartita() {
        if (gestore.isVittoria()) {
            txtLog.appendText("\n🎉 VITTORIA! Hai violato il Mainframe Riservato!\n");
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "🎉 VITTORIA! Hai completato l'obiettivo!", ButtonType.OK);
            alert.showAndWait();
        } else if (gestore.isSconfitta()) {
            txtLog.appendText("\n☠️ SCONFITTA! L'energia dell'Hacker si è azzerata.\n");
            Alert alert = new Alert(Alert.AlertType.ERROR, "☠️ SCONFITTA! L'energia dell'Hacker si è azzerata.", ButtonType.OK);
            alert.showAndWait();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
