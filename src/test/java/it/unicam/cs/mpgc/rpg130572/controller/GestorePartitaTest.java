package it.unicam.cs.mpgc.rpg130572.controller;

import it.unicam.cs.mpgc.rpg130572.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GestorePartitaTest {

    private MappaRete mappa;
    private Hacker hacker;
    private GestorePartita gestore;

    private static class DummyNodo extends NodoRete {
        public DummyNodo(String id, String nome, int sicurezza, String tipo) {
            super(id, nome, sicurezza, tipo);
        }

        @Override
        public String ottieniDescrizione() {
            return getId() + " - " + getNome();
        }
    }

    @BeforeEach
    void setUp() {
        mappa = new MappaRete();

        NodoRete n1 = new DummyNodo("NODO_01", "Gateway", 1, "NORMALE");
        NodoRete n2 = new DummyNodo("NODO_02", "Trap Node", 1, "TRAPPOLA");
        NodoRete n3 = new DummyNodo("NODO_03", "Mainframe", 3, "MAINFRAME");

        mappa.aggiungiNodo(n1);
        mappa.aggiungiNodo(n2);
        mappa.aggiungiNodo(n3);

        mappa.collegaNodi("NODO_01", "NODO_02");
        mappa.collegaNodi("NODO_01", "NODO_03");

        hacker = new Hacker("Ghost", 100, 2);
        gestore = new GestorePartita(hacker, mappa, "NODO_03");
        gestore.spostaHacker("NODO_01");
    }

    @Test
    void testSpostamentoTraNodi() {
        assertTrue(gestore.spostaHacker("NODO_02"));
        assertEquals("NODO_02", hacker.getNodoCorrente().getId());
        assertFalse(gestore.spostaHacker("NODO_INESISTENTE"));
    }

    @Test
    void testScansioneNodoTrappola() {
        int energiaIniziale = hacker.getEnergia();
        String esito = gestore.scansionaNodo("NODO_02");

        assertEquals(energiaIniziale - 5, hacker.getEnergia());
        assertTrue(esito.contains("ANOMALIA RILEVATA") || esito.contains("Scansione completata"));
    }

    @Test
    void testHackingNodoNormaleEIncrementoAbilita() {
        assertEquals(2, hacker.getLivelloAbilita());

        String esito = gestore.tentaHackingNodoCorrente();
        assertTrue(esito.contains("completato con successo"));
        assertTrue(hacker.getNodoCorrente().isHackerato());
        assertEquals(3, hacker.getLivelloAbilita());
    }

    @Test
    void testHackingNodoTrappola() {
        gestore.spostaHacker("NODO_02");
        int energiaIniziale = hacker.getEnergia();

        String esito = gestore.tentaHackingNodoCorrente();
        assertTrue(esito.contains("TRAPPOLA ATTIVATA"));
        assertEquals(energiaIniziale - 30, hacker.getEnergia());
    }

    @Test
    void testCondizioniVittoriaESconfitta() {
        assertFalse(gestore.isVittoria());
        assertFalse(gestore.isSconfitta());

        hacker.riduciEnergia(100);
        assertTrue(gestore.isSconfitta());
    }
}