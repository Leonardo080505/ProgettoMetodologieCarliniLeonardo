package it.unicam.cs.mpgc.rpg130572.model;

/**
 * Rappresenta un generico nodo all'interno della rete Cyberpunk.
 * Questa classe astratta fa da base per i vari tipi di server e nodi di sicurezza.
 */
public abstract class NodoRete {
    private final String id;
    private final String nome;
    private int livelloSicurezza;
    private boolean hackerato;

    public NodoRete(String id, String nome, int livelloSicurezza) {
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

    /**
     * Metodo astratto che implementerà ogni tipo specifico di nodo (es. ServerDati, Firewall)
     * con la propria descrizione personalizzata.
     */
    public abstract String ottieniDescrizione();
}