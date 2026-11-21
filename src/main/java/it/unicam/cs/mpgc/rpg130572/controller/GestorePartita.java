package it.unicam.cs.mpgc.rpg130572.controller;

import it.unicam.cs.mpgc.rpg130572.model.*;
import java.util.List;

/**
 * Gestisce la logica del gioco e le azioni dell'Hacker all'interno della MappaRete.
 */
public class GestorePartita {

    private final Hacker hacker;
    private final MappaRete mappa;
    private final String idNodoObiettivo;

    public GestorePartita(Hacker hacker, MappaRete mappa, String idNodoObiettivo) {
        if (hacker == null || mappa == null || idNodoObiettivo == null) {
            throw new IllegalArgumentException("I parametri del gestore partita non possono essere null.");
        }
        this.hacker = hacker;
        this.mappa = mappa;
        this.idNodoObiettivo = idNodoObiettivo;
    }

    public Hacker getHacker() {
        return hacker;
    }

    public String getIdNodoObiettivo() {
        return idNodoObiettivo;
    }

    public MappaRete getMappa() {
        return mappa;
    }

    public boolean spostaHacker(String idNodoDestinazione) {
        if (hacker.getNodoCorrente() == null) {
            NodoRete nodoIniziale = mappa.getNodo(idNodoDestinazione);
            if (nodoIniziale != null) {
                hacker.setNodoCorrente(nodoIniziale);
                return true;
            }
            return false;
        }

        List<NodoRete> adiacenti = mappa.getNodiAdiacenti(hacker.getNodoCorrente().getId());
        for (NodoRete n : adiacenti) {
            if (n.getId().equalsIgnoreCase(idNodoDestinazione)) {
                hacker.setNodoCorrente(n);
                return true;
            }
        }
        return false;
    }

    public String tentaHackingNodoCorrente() {
        NodoRete corrente = hacker.getNodoCorrente();
        if (corrente == null) return "Nessun nodo selezionato.";
        if (corrente.isHackerato()) return "Il nodo è già stato violato!";

        if (corrente.isTrappola()) {
            hacker.riduciEnergia(30);
            return " TRAPPOLA ATTIVATA! Il nodo era una trappola . Subisci 30 danni !";
        }

        int costoEnergia = corrente.getLivelloSicurezza() * 10;
        if (hacker.getEnergia() < costoEnergia) {
            return "Energia insufficiente per violare il nodo (richiesti " + costoEnergia + " energia).";
        }

        if (hacker.getLivelloAbilita() >= corrente.getLivelloSicurezza()) {
            corrente.setHackerato(true);

            int bonus = corrente.getBonusAbilita();
            for (int i = 0; i < bonus; i++) {
                hacker.incrementaAbilita();
            }

            String msgBonus = (bonus > 1) ? "  BONUS SERVER (+2 Abilità)!" : "";
            return "Hacking completato con successo sul nodo " + corrente.getId()
                    + "!" + msgBonus + "  Abilità Hacker aumentata a " + hacker.getLivelloAbilita() + "!";
        } else {
            hacker.riduciEnergia(15);
            return "Hacking fallito su " + corrente.getId() + "! Livello abilità insufficiente (richiesto "
                    + corrente.getLivelloSicurezza() + "). Subisci 15 danni.";
        }
    }

    public String scansionaNodoTarget(String idNodoTarget) {
        hacker.riduciEnergia(5);
        NodoRete nodo = mappa.getNodo(idNodoTarget);
        if (nodo == null) return "Nodo inesistente.";

        if (nodo.isTrappola()) {
            if (hacker.getLivelloAbilita() >= 2) {
                return " ANOMALIA RILEVATA su " + idNodoTarget + ": Firma di rete sospetta! Potrebbe essere una TRAPPOLA.";
            }
        }
        return " Scansione completata su " + idNodoTarget + ": Nessuna anomalia evidente rilevata.";
    }

    public String scansionaNodo(String idNodoTarget) {
        return scansionaNodoTarget(idNodoTarget);
    }

    public boolean isVittoria() {
        NodoRete obiettivo = mappa.getNodo(idNodoObiettivo);
        return obiettivo != null && obiettivo.isHackerato();
    }

    public boolean isSconfitta() {
        return !hacker.isAttivo();
    }
}