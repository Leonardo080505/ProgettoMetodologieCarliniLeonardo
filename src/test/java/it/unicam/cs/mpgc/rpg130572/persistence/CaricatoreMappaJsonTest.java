package it.unicam.cs.mpgc.rpg130572.persistence;

import it.unicam.cs.mpgc.rpg130572.model.MappaRete;
import it.unicam.cs.mpgc.rpg130572.model.NodoRete;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CaricatoreMappaJsonTest {

    @Test
    void testCaricamentoMappaDaRisorse() {
        CaricatoreMappaJson caricatore = new CaricatoreMappaJson();
        MappaRete mappa = caricatore.caricaDaRisorse("mappa.json");

        assertNotNull(mappa);

        NodoRete nodoIngresso = mappa.getNodo("NODO_01");
        assertNotNull(nodoIngresso);

        NodoRete mainframe = mappa.getNodo("NODO_07");
        assertNotNull(mainframe);

        assertFalse(mappa.getNodiAdiacenti("NODO_01").isEmpty());
    }
}