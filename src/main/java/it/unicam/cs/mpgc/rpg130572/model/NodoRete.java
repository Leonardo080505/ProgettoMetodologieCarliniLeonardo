package it.unicam.cs.mpgc.rpg130572.model;

import java.util.Objects;

/**
 * Rappresenta un generico nodo all'interno della rete .
 */
public abstract class NodoRete {
    private final String id;
    private final String nome;
    private int livelloSicurezza;
    private boolean hackerato;
    private final String tipo;


    public NodoRete(String id, String nome, int livelloSicurezza, String tipo) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("L'ID del nodo non può essere vuoto.");
        }
        if (livelloSicurezza < 0) {
            throw new IllegalArgumentException("Il livello di sicurezza non può essere negativo.");
        }
        this.id = id;
        this.nome = nome;
        this.livelloSicurezza = livelloSicurezza;
        this.hackerato = false;
        this.tipo = (tipo != null) ? tipo : "NORMALE";
    }


    public NodoRete(String id, String nome, int livelloSicurezza) {
        this(id, nome, livelloSicurezza, "NORMALE");
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public int getLivelloSicurezza() {
        return livelloSicurezza;
    }

    public boolean isHackerato() {
        return hackerato;
    }

    public void setHackerato(boolean hackerato) {
        this.hackerato = hackerato;
    }

    public String getTipo() {
        return tipo;
    }

    public boolean isTrappola() {
        return "TRAPPOLA".equalsIgnoreCase(tipo);
    }

    /**
     * Metodo astratto che implementerà ogni tipo specifico di nodo (es. ServerDati, Firewall)
     * con la propria descrizione personalizzata.
     */
    public abstract String ottieniDescrizione();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NodoRete nodoRete = (NodoRete) o;
        return Objects.equals(id, nodoRete.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}