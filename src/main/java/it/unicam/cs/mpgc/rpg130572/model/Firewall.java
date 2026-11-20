package it.unicam.cs.mpgc.rpg130572.model;

/**
 * Rappresenta una difesa di rete che infligge danno.
 */
public class Firewall extends NodoRete {

    private final int danno;

    public Firewall(String id, String nome, int livelloSicurezza, int danno) {
        super(id, nome, livelloSicurezza, "FIREWALL");
        if (danno < 0) {
            throw new IllegalArgumentException("Il danno del firewall non può essere negativo.");
        }
        this.danno = danno;
    }

    public int getDanno() {
        return danno;
    }

    @Override
    public String ottieniDescrizione() {
        return "Firewall [" + getNome() + "] - Danno : " + danno +
                " | Livello Sicurezza: " + getLivelloSicurezza() +
                " | Stato: " + (isHackerato() ? "Disattivato" : "Attivo");
    }
}