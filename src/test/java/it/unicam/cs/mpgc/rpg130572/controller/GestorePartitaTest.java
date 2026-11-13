package it.unicam.cs.mpgc.rpg130572.controller;

import it.unicam.cs.mpgc.rpg130572.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GestorePartitaTest {

    private Hacker hacker;
    private MappaRete mappa;
    private ServerDati ingresso;
    private Firewall firewall;
    private ServerDati obiettivo;
    private GestorePartita gestore;

    @BeforeEach
    void setUp() {
        // Inizializzazione prima di ogni test
        hacker = new Hacker("CyberTest", 50, 2);
        mappa = new MappaRete();

        ingresso = new ServerDati("NODO_01", "Start", 1, "Data", 10);
        firewall = new Firewall("NODO_02", "Firewall", 3, 20);
        obiettivo = new ServerDati("NODO_03", "Mainframe", 4, "Secret", 100);

        mappa.aggiungiNodo(ingresso);
        mappa.aggiungiNodo(firewall);
        mappa.aggiungiNodo(obiettivo);

        mappa.collegaNodi("NODO_01", "NODO_02");
        mappa.collegaNodi("NODO_02", "NODO_03");

        gestore = new GestorePartita(hacker, mappa, "NODO_03");
        gestore.spostaHacker("NODO_01");
    }

    @Test
    void testSpostamentoValidoEInvalido() {
        // L'hacker parte da NODO_01.
        // Tentativo di spostamento verso NODO_03 (non adiacente a NODO_01) -> Deve fallire (false)
        assertFalse(gestore.spostaHacker("NODO_03"));
        assertEquals("NODO_01", hacker.getNodoCorrente().getId());

        // Spostamento verso NODO_02 (adiacente a NODO_01) -> Deve avere successo (true)
        assertTrue(gestore.spostaHacker("NODO_02"));
        assertEquals("NODO_02", hacker.getNodoCorrente().getId());
    }

    @Test
    void testHackingRiuscitoEAumentoAbilita() {
        // L'abilità dell'hacker (2) è maggiore della sicurezza di NODO_01 (1)
        int abilitaIniziale = hacker.getLivelloAbilita();
        String esito = gestore.tentaHackingNodoCorrente();

        assertTrue(ingresso.isHackerato());
        assertTrue(esito.contains("riuscito"));
        assertEquals(abilitaIniziale + 1, hacker.getLivelloAbilita());
    }

    @Test
    void testHackingFallitoEDannoFirewall() {
        // Ci spostiamo sul Firewall (sicurezza 3, danno 20)
        gestore.spostaHacker("NODO_02");
        int energiaIniziale = hacker.getEnergia();

        // L'abilità dell'hacker è 2, inferiore alla sicurezza 3
        String esito = gestore.tentaHackingNodoCorrente();

        assertFalse(firewall.isHackerato());
        assertTrue(esito.contains("fallito"));
        assertEquals(energiaIniziale - 20, hacker.getEnergia());
    }

    @Test
    void testCondizioneVittoria() {
        assertFalse(gestore.isVittoria());

        // Forziamo lo stato hackerato sull'obiettivo
        obiettivo.setHackerato(true);

        assertTrue(gestore.isVittoria());
    }

    @Test
    void testCondizioneSconfitta() {
        assertFalse(gestore.isSconfitta());

        // Azzeriamo l'energia dell'hacker
        hacker.riduciEnergia(100);

        assertTrue(gestore.isSconfitta());
    }

    @Test
    void testSalvataggioPartita() {
        it.unicam.cs.mpgc.rpg130572.persistence.GestoreSalvataggioJson salvataggio =
                new it.unicam.cs.mpgc.rpg130572.persistence.GestoreSalvataggioJson();

        assertDoesNotThrow(() -> salvataggio.salvaPartita(gestore, "salvataggio_test.json"));

        java.io.File file = new java.io.File("salvataggio_test.json");
        assertTrue(file.exists());
        file.deleteOnExit(); // Rimuove il file temporaneo dopo il test
    }
}