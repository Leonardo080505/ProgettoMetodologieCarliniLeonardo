package it.unicam.cs.mpgc.rpg130572.model;

/**
 * Server che il giocatore puo hackerare
 * Estende la classe astratta NodoRete implementando la propria descrizione.
 */
public class ServerDati extends NodoRete {
    private final String tipoContenuto;
    private final int valoreDati;

    public ServerDati(String id, String nome, int livelloSicurezza, String tipoContenuto, int valoreDati) {
        super(id, nome, livelloSicurezza);
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
}