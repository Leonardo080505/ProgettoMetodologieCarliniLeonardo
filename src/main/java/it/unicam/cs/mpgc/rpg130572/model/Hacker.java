package it.unicam.cs.mpgc.rpg130572.model;

import java.util.Objects;

/**
 * Gestisce lo stato dell'hacker nel gioco, inclusi punti energia e livello di abilità.
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
            throw new IllegalArgumentException("Il livello di abilità iniziale deve essere almeno 1.");
        }
        this.nickname = nickname;
        this.puntiEnergia = puntiEnergia;
        this.livelloAbilita = livelloAbilita;
    }

    public String getNickname() {
        return nickname;
    }

    public int getEnergia() {
        return puntiEnergia;
    }

    public int getLivelloAbilita() {
        return livelloAbilita;
    }

    public NodoRete getNodoCorrente() {
        return nodoCorrente;
    }

    public void setNodoCorrente(NodoRete nodoCorrente) {
        this.nodoCorrente = Objects.requireNonNull(nodoCorrente, "Il nodo corrente non può essere null.");
    }

    public void riduciEnergia(int quantita) {
        this.puntiEnergia = Math.max(0, this.puntiEnergia - quantita);
    }

    public void incrementaAbilita() {
        this.livelloAbilita++;
    }

    public boolean isAttivo() {
        return puntiEnergia > 0;
    }
}