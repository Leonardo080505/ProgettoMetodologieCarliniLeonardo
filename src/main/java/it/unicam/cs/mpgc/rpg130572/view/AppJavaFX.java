package it.unicam.cs.mpgc.rpg130572.view;

import it.unicam.cs.mpgc.rpg130572.controller.GestorePartita;
import it.unicam.cs.mpgc.rpg130572.model.*;
import it.unicam.cs.mpgc.rpg130572.persistence.CaricatoreMappaJson;
import it.unicam.cs.mpgc.rpg130572.persistence.GestoreSalvataggioJson;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;

public class AppJavaFX extends Application {

    private Stage stagePrincipale;
    private MappaRete mappa;
    private GestorePartita gestore;

    // Componenti della Schermata di Gioco
    private Label lblStatoHacker;
    private Label lblNodoAttuale;
    private TextArea txtLog;
    private VBox boxAdiacenti;

    @Override
    public void start(Stage primaryStage) {
        this.stagePrincipale = primaryStage;
        primaryStage.setTitle("Cyberpunk Network Hacker - GUI");

        mostraSchermataIniziale();
    }

    private void mostraSchermataIniziale() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(30));

        Label titleLabel = new Label("=== CYBERPUNK NETWORK HACKER ===");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        VBox topBox = new VBox(titleLabel);
        topBox.setAlignment(Pos.CENTER);
        root.setTop(topBox);

        Label infoLabel = new Label("Benvenuto! Seleziona un'opzione per iniziare:");
        infoLabel.setStyle("-fx-font-size: 14px;");

        Button btnNuovaPartita = new Button("🎮 Nuova Partita");
        btnNuovaPartita.setStyle("-fx-font-size: 14px; -fx-padding: 8 20;");
        btnNuovaPartita.setOnAction(e -> iniziaNuovaPartita());

        Button btnCaricaPartita = new Button("📂 Carica Partita (mappa.json)");
        btnCaricaPartita.setStyle("-fx-font-size: 14px; -fx-padding: 8 20;");
        btnCaricaPartita.setOnAction(e -> iniziaNuovaPartita());

        VBox centerBox = new VBox(15, infoLabel, btnNuovaPartita, btnCaricaPartita);
        centerBox.setAlignment(Pos.CENTER);
        root.setCenter(centerBox);

        Scene scene = new Scene(root, 650, 450);
        stagePrincipale.setScene(scene);
        stagePrincipale.show();
    }

    private void iniziaNuovaPartita() {
        CaricatoreMappaJson caricatore = new CaricatoreMappaJson();
        this.mappa = caricatore.caricaDaRisorse("mappa.json");
        Hacker hacker = new Hacker("CyberZero", 100, 2);

        this.gestore = new GestorePartita(hacker, mappa, "NODO_03");
        this.gestore.spostaHacker("NODO_01");

        mostraSchermataGioco();
    }

    private void mostraSchermataGioco() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(15));

        // Intestazione con info Hacker e Posizione
        lblStatoHacker = new Label();
        lblStatoHacker.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        lblNodoAttuale = new Label();
        lblNodoAttuale.setStyle("-fx-font-size: 13px;");

        VBox topBox = new VBox(8, lblStatoHacker, lblNodoAttuale);
        topBox.setPadding(new Insets(0, 0, 10, 0));
        root.setTop(topBox);

        // Centro: Registro Eventi (Log)
        txtLog = new TextArea();
        txtLog.setEditable(false);
        txtLog.appendText("Partita Iniziata!\nObiettivo: Violare il Mainframe (NODO_03)\n--------------------------------------------------\n");

        // Lato Destro: Pulsanti Spostamento e Azioni
        Label lblVicini = new Label("Spostati in:");
        lblVicini.setStyle("-fx-font-weight: bold;");

        boxAdiacenti = new VBox(8);

        Button btnHack = new Button("⚡ Tenta Hacking");
        btnHack.setMaxWidth(Double.MAX_VALUE);
        btnHack.setStyle("-fx-background-color: #ff9800; -fx-text-fill: white; -fx-font-weight: bold;");
        btnHack.setOnAction(e -> eseguiHacking());

        Button btnSalva = new Button("💾 Salva Partita");
        btnSalva.setMaxWidth(Double.MAX_VALUE);
        btnSalva.setOnAction(e -> eseguiSalvataggio());

        VBox rightBox = new VBox(10, lblVicini, boxAdiacenti, new Separator(), btnHack, btnSalva);
        rightBox.setPadding(new Insets(0, 0, 0, 15));
        rightBox.setPrefWidth(220);

        root.setCenter(txtLog);
        root.setRight(rightBox);

        aggiornaInterfaccia();

        Scene scene = new Scene(root, 720, 480);
        stagePrincipale.setScene(scene);
    }

    private void aggiornaInterfaccia() {
        Hacker h = gestore.getHacker();
        lblStatoHacker.setText("HACKER: " + h.getNickname() + " | Energia: " + h.getEnergia() + " | Abilità: " + h.getLivelloAbilita());

        NodoRete nodo = h.getNodoCorrente();
        if (nodo != null) {
            lblNodoAttuale.setText("POSIZIONE ATTUALE: " + nodo.ottieniDescrizione());
        }

        // Genera dinamicamente i pulsanti per i nodi adiacenti raggiungibili
        boxAdiacenti.getChildren().clear();
        if (nodo != null) {
            List<NodoRete> vicini = mappa.getNodiAdiacenti(nodo.getId());
            for (NodoRete v : vicini) {
                Button btnSposta = new Button("➡️ " + v.getId() + " (" + v.getNome() + ")");
                btnSposta.setMaxWidth(Double.MAX_VALUE);
                btnSposta.setOnAction(ev -> {
                    if (gestore.spostaHacker(v.getId())) {
                        txtLog.appendText("[SPOSTAMENTO]: Spostato nel nodo " + v.getId() + "\n");
                        aggiornaInterfaccia();
                    }
                });
                boxAdiacenti.getChildren().add(btnSposta);
            }
        }
    }

    private void eseguiHacking() {
        String esito = gestore.tentaHackingNodoCorrente();
        txtLog.appendText("[AZIONALI HACKING]: " + esito + "\n");
        aggiornaInterfaccia();
        controllaEsitoPartita();
    }

    private void eseguiSalvataggio() {
        GestoreSalvataggioJson salvataggio = new GestoreSalvataggioJson();
        salvataggio.salvaPartita(gestore, "salvataggio.json");
        txtLog.appendText("[PERSISTENZA]: Partita salvata con successo in salvataggio.json!\n");
    }

    private void controllaEsitoPartita() {
        if (gestore.isVittoria()) {
            txtLog.appendText("\n🎉 VITTORIA! Hai completato l'obiettivo!\n");
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "🎉 VITTORIA! Hai violato il Mainframe Centrale!", ButtonType.OK);
            alert.showAndWait();
        } else if (gestore.isSconfitta()) {
            txtLog.appendText("\n☠️ SCONFITTA! L'energia dell'Hacker è esaurita.\n");
            Alert alert = new Alert(Alert.AlertType.ERROR, "☠️ SCONFITTA! L'energia dell'Hacker si è azzerata.", ButtonType.OK);
            alert.showAndWait();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}