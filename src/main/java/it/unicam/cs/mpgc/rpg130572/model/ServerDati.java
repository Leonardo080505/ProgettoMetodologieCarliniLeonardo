package it.unicam.cs.mpgc.rpg130572.model;

/**
 * Rappresenta un server di dati cche il giocatore puo hackerare per ottenere risorse.
 */
public class ServerDati extends NodoRete {

    private final String tipoContenuto;
    private final int valoreDati;

    public ServerDati(String id, String nome, int livelloSicurezza, String tipoContenuto, int valoreDati) {
        super(id, nome, livelloSicurezza, "SERVER");
        if (tipoContenuto == null || tipoContenuto.isBlank()) {
            throw new IllegalArgumentException("Il tipo di contenuto non può essere vuoto.");
        }
        if (valoreDati < 0) {
            throw new IllegalArgumentException("Il valore dei dati non può essere negativo.");
        }
        this.tipoContenuto = tipoContenuto;
        this.valoreDati = valoreDati;
    }

    public String getTipoContenuto() {
        return tipoContenuto;
    }

    public int getValoreDati() {
        return valoreDati;
    }

    @Override
    public String ottieniDescrizione() {
        return "Server Dati [" + getNome() + "] - Tipo: " + tipoContenuto +
                " | Valore: " + valoreDati + " crediti | Stato: " +
                (isHackerato() ? "Hackerato" : "Protetto");
    }

    @Override
    public int getBonusAbilita() {
        return 2; // I Server Dati forniscono +2 punti abilità!
    }
}