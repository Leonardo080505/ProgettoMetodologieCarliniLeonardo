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
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
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

    private final Color COLOR_BG_DARK = Color.web("#0b0c10");
    private final Color COLOR_PANEL_BG = Color.rgb(31, 40, 51, 0.85);
    private final Color COLOR_CARD_BG = Color.web("#1f2833");
    private final Color COLOR_CYAN = Color.web("#66fcf1");
    private final Color COLOR_BORDER_CYAN = Color.web("#45f3ff");
    private final Color COLOR_TEXT_LIGHT = Color.web("#c5c6c7");
    private final Color COLOR_RED = Color.web("#ff2a6d");
    private final Color COLOR_RED_HOVER = Color.web("#ff5c8d");

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
        root.setBackground(Background.EMPTY);

        Label titleLabel = new Label("=== NETWORK HACKER ===");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 26));
        titleLabel.setTextFill(COLOR_CYAN);

        DropShadow shadow = new DropShadow();
        shadow.setColor(COLOR_CYAN);
        shadow.setRadius(10);
        shadow.setSpread(0.2);
        titleLabel.setEffect(shadow);

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
        infoLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        infoLabel.setTextFill(COLOR_TEXT_LIGHT);

        Button btnNuovaPartita = new Button("Nuova Partita");
        stilaBottone(btnNuovaPartita, COLOR_CARD_BG, COLOR_CYAN, COLOR_CYAN);
        btnNuovaPartita.setFont(Font.font("System", FontWeight.BOLD, 14));
        btnNuovaPartita.setPadding(new Insets(10, 20, 10, 20));
        btnNuovaPartita.setOnAction(event -> iniziaNuovaPartita());

        Button btnCaricaPartita = new Button("Carica Partita da JSON");
        stilaBottone(btnCaricaPartita, COLOR_CARD_BG, COLOR_CYAN, COLOR_CYAN);
        btnCaricaPartita.setFont(Font.font("System", FontWeight.BOLD, 14));
        btnCaricaPartita.setPadding(new Insets(10, 20, 10, 20));
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
        root.setBackground(Background.EMPTY);

        lblStatoHacker = new Label();
        lblStatoHacker.setFont(Font.font("System", FontWeight.BOLD, 14));
        lblStatoHacker.setTextFill(COLOR_CYAN);

        lblNodoAttuale = new Label();
        lblNodoAttuale.setFont(Font.font("System", FontWeight.NORMAL, 13));
        lblNodoAttuale.setTextFill(COLOR_TEXT_LIGHT);
        lblNodoAttuale.setBackground(new Background(new BackgroundFill(COLOR_PANEL_BG, new CornerRadii(4), Insets.EMPTY)));
        lblNodoAttuale.setBorder(new Border(new BorderStroke(COLOR_CYAN, BorderStrokeStyle.SOLID, new CornerRadii(4), new BorderWidths(1))));
        lblNodoAttuale.setPadding(new Insets(10));

        VBox topBox = new VBox(8, lblStatoHacker, lblNodoAttuale);
        topBox.setPadding(new Insets(0, 0, 10, 0));
        root.setTop(topBox);

        txtLog = new TextArea();
        txtLog.setEditable(false);
        txtLog.setFont(Font.font("Consolas", 12));
        txtLog.setBorder(new Border(new BorderStroke(COLOR_BORDER_CYAN, BorderStrokeStyle.SOLID, new CornerRadii(4), new BorderWidths(1))));
        txtLog.appendText(messaggioIniziale + "\nObiettivo: Violare il Mainframe Riservato (NODO_13)\n--------------------------------------------------\n");

        Label lblVicini = new Label("Spostati / Scansiona:");
        lblVicini.setFont(Font.font("System", FontWeight.BOLD, 13));
        lblVicini.setTextFill(COLOR_CYAN);

        boxAdiacenti = new VBox(8);

        btnHack = new Button("Tenta Hacking");
        stilaBottoneSpeciale(btnHack, COLOR_RED, Color.WHITE, COLOR_RED_HOVER);
        btnHack.setOnAction(event -> {
            String esito = gestore.tentaHackingNodoCorrente();
            txtLog.appendText(esito + "\n");
            aggiornaInterfaccia();
            controllaEsitoPartita();
        });

        Button btnSalva = new Button("Salva Partita");
        stilaBottone(btnSalva, COLOR_CARD_BG, COLOR_CYAN, COLOR_CYAN);
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
                stilaBottone(btnSposta, COLOR_CARD_BG, COLOR_TEXT_LIGHT, COLOR_BORDER_CYAN);

                btnSposta.setOnAction(event -> {
                    if (gestore.spostaHacker(v.getId())) {
                        txtLog.appendText("[SPOSTAMENTO]: Spostato nel nodo " + v.getId() + "\n");
                        aggiornaInterfaccia();
                    }
                });

                Button btnScansiona = new Button("Scansiona");
                stilaBottone(btnScansiona, COLOR_CARD_BG, COLOR_CYAN, COLOR_CYAN);
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

    private void stilaBottone(Button btn, Color bgColor, Color textColor, Color borderColor) {
        Background bgNormal = new Background(new BackgroundFill(bgColor, new CornerRadii(4), Insets.EMPTY));
        Border borderNormal = new Border(new BorderStroke(borderColor, BorderStrokeStyle.SOLID, new CornerRadii(4), new BorderWidths(1)));

        Background bgHover = new Background(new BackgroundFill(borderColor, new CornerRadii(4), Insets.EMPTY));
        Border borderHover = new Border(new BorderStroke(borderColor, BorderStrokeStyle.SOLID, new CornerRadii(4), new BorderWidths(1)));

        btn.setFont(Font.font("System", FontWeight.BOLD, 12));
        btn.setTextFill(textColor);
        btn.setBackground(bgNormal);
        btn.setBorder(borderNormal);
        btn.setPadding(new Insets(6, 10, 6, 10));

        btn.setOnMouseEntered(e -> {
            btn.setBackground(bgHover);
            btn.setBorder(borderHover);
            btn.setTextFill(COLOR_BG_DARK);
        });

        btn.setOnMouseExited(e -> {
            btn.setBackground(bgNormal);
            btn.setBorder(borderNormal);
            btn.setTextFill(textColor);
        });
    }

    private void stilaBottoneSpeciale(Button btn, Color bgColor, Color textColor, Color hoverBgColor) {
        Background bgNormal = new Background(new BackgroundFill(bgColor, new CornerRadii(4), Insets.EMPTY));
        Border borderNormal = new Border(new BorderStroke(bgColor, BorderStrokeStyle.SOLID, new CornerRadii(4), new BorderWidths(1)));

        Background bgHover = new Background(new BackgroundFill(hoverBgColor, new CornerRadii(4), Insets.EMPTY));
        Border borderHover = new Border(new BorderStroke(hoverBgColor, BorderStrokeStyle.SOLID, new CornerRadii(4), new BorderWidths(1)));

        btn.setFont(Font.font("System", FontWeight.BOLD, 13));
        btn.setTextFill(textColor);
        btn.setBackground(bgNormal);
        btn.setBorder(borderNormal);
        btn.setPadding(new Insets(8, 12, 8, 12));

        btn.setOnMouseEntered(e -> {
            btn.setBackground(bgHover);
            btn.setBorder(borderHover);
        });

        btn.setOnMouseExited(e -> {
            btn.setBackground(bgNormal);
            btn.setBorder(borderNormal);
        });
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
            txtLog.appendText("\nSconfitta! Hai esaurito l'energia.\n");
            Alert alert = new Alert(Alert.AlertType.ERROR, "Sconfitta! Hai esaurito l'energia.", ButtonType.OK);
            alert.showAndWait();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}