package it.unicam.cs.mpgc.rpg130572.controller;

import it.unicam.cs.mpgc.rpg130572.model.*;
import java.util.List;

/**
 * Gestisce la logica del gioco e le azioni dell'Hacker
 * all'interno della MappaRete.
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

    /**
     * Sposta l'hacker in un nodo adiacente a quello corrente.
     */
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
        for (NodoRete nodo : adiacenti) {
            if (nodo.getId().equals(idNodoDestinazione)) {
                hacker.setNodoCorrente(nodo);
                return true;
            }
        }
        return false;
    }

    /**
     * Esegue il tentativo di hacking sul nodo in cui si trova l'hacker.
     */
    public String tentaHackingNodoCorrente() {
        NodoRete corrente = hacker.getNodoCorrente();
        if (corrente == null) return "Nessun nodo selezionato.";
        if (corrente.isHackerato()) return "Il nodo è già stato violato!";

        if (corrente.isTrappola()) {
            hacker.riduciEnergia(30);
            return "💥 TRAPPOLA ATTIVATA! Il nodo era un Honeypot. Subiti 30 danni da contromisura!";
        }

        int costoEnergia = corrente.getLivelloSicurezza() * 10;
        if (hacker.getEnergia() < costoEnergia) {
            return "Energia insufficiente per violare il nodo (richiesti " + costoEnergia + " energia).";
        }

        if (hacker.getLivelloAbilita() >= corrente.getLivelloSicurezza()) {
            corrente.setHackerato(true);
            hacker.incrementaAbilita(); // 👈 Aumenta il livello abilità!
            return "Hacking completato con successo sul nodo " + corrente.getId()
                    + "! 📈 Abilità Hacker aumentata a " + hacker.getLivelloAbilita() + "!";
        } else {
            hacker.riduciEnergia(15);
            return "Hacking fallito! Livello abilità insufficiente (richiesto "
                    + corrente.getLivelloSicurezza() + "). Subiti 15 danni.";
        }
    }

    public boolean isVittoria() {
        NodoRete obiettivo = mappa.getNodo(idNodoObiettivo);
        return obiettivo != null && obiettivo.isHackerato();
    }

    public String scansionaNodo(String idNodoTarget) {
        if (hacker.getEnergia() < 5) {
            return "Energia insufficiente per la scansione (servono 5 energia).";
        }

        hacker.riduciEnergia(5);
        NodoRete nodo = mappa.getNodo(idNodoTarget);
        if (nodo == null) return "Nodo inesistente.";

        // La probabilità di rilevare la trappola cresce con l'abilità dell'Hacker
        if (nodo.isTrappola()) {
            if (hacker.getLivelloAbilita() >= 2) {
                return "⚠️ ANOMALIA RILEVATA su " + idNodoTarget + ": Firma di rete sospetta! Potrebbe essere una TRAPPOLA.";
            }
        }
        return "✅ Scansione completata su " + idNodoTarget + ": Nessuna anomalia evidente rilevata.";
    }

    public boolean isSconfitta() {
        return !hacker.isAttivo();
    }
}