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
        if (corrente == null) {
            return "Nessun nodo corrente selezionato.";
        }
        if (corrente.isHackerato()) {
            return "Il nodo è già stato violato!";
        }

        if (hacker.getLivelloAbilita() >= corrente.getLivelloSicurezza()) {
            corrente.setHackerato(true);
            hacker.incrementaLivelloAbilita();
            return "Hacking riuscito su " + corrente.getNome() + "! Livello abilità aumentato.";
        } else {
            if (corrente instanceof Firewall firewall) {
                hacker.riduciEnergia(firewall.getDanno());
                return "Attacco fallito! Il Firewall ha inflitto " + firewall.getDanno() + " danni d'energia.";
            }
            hacker.riduciEnergia(5);
            return "Attacco fallito! Persi 5 punti energia.";
        }
    }

    public boolean isVittoria() {
        NodoRete obiettivo = mappa.getNodo(idNodoObiettivo);
        return obiettivo != null && obiettivo.isHackerato();
    }

    public boolean isSconfitta() {
        return !hacker.isAttivo();
    }
}