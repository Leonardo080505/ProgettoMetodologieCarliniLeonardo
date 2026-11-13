package it.unicam.cs.mpgc.rpg130572.model;

import java.util.Objects;

/**
 * Rappresenta il personaggio giocabile (Hacker) nel sistema
 * Gestisce lo stato del giocatore, i punti energia, il livello di abilità
 * e il nodo di rete in cui si trova  attualmente il giocatore.
 */
public class Hacker {
    private final String nickname;
    private int puntiEnergia;
    private int livelloAbilita;
    private NodoRete nodoCorrente;

    public Hacker(String nickname, int puntiEnergia, int livelloAbilita) {
        if (nickname == null || nickname.isBlank()) {
            throw new IllegalArgumentException("Il nickname dell'hacker non può essere vuoto.");
        }
        if (puntiEnergia <= 0) {
            throw new IllegalArgumentException("I punti energia iniziali devono essere maggiori di zero.");
        }
        if (livelloAbilita < 1) {
            throw new IllegalArgumentException("Il livello di abilità deve essere almeno 1.");
        }
        this.nickname = nickname;
        this.puntiEnergia = puntiEnergia;
        this.livelloAbilita = livelloAbilita;
        this.nodoCorrente = null;
    }

    public String getNickname() {
        return nickname;
    }

    public int getEnergia() {
        return puntiEnergia;
    }

    public void riduciEnergia(int quantita) {
        if (quantita < 0) {
            throw new IllegalArgumentException("La quantità di energia da ridurre non può essere negativa.");
        }
        this.puntiEnergia = Math.max(0, this.puntiEnergia - quantita);
    }

    public void ricaricaEnergia(int quantita) {
        if (quantita < 0) {
            throw new IllegalArgumentException("La quantità di energia da ricaricare non può essere negativa.");
        }
        this.puntiEnergia += quantita;
    }

    public int getLivelloAbilita() {
        return livelloAbilita;
    }

    public void incrementaLivelloAbilita() {
        this.livelloAbilita++;
    }

    public NodoRete getNodoCorrente() {
        return nodoCorrente;
    }

    public void setNodoCorrente(NodoRete nodoCorrente) {
        this.nodoCorrente = Objects.requireNonNull(nodoCorrente, "Il nodo corrente non può essere null.");
    }

    public boolean isAttivo() {
        return puntiEnergia > 0;
    }
}