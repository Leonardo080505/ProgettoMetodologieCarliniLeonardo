package it.unicam.cs.mpgc.rpg130572.model;

import java.util.*;

/**
 * Rappresenta la rete di nodi all'interno del gioco.
 * Gestisce i nodi presenti e le connessioni orientate o bidirezionali tra di essi.
 */
public class MappaRete {
    private final Map<String, NodoRete> nodi;
    private final Map<String, List<String>> adiacenze;

    public MappaRete() {
        this.nodi = new HashMap<>();
        this.adiacenze = new HashMap<>();
    }

    /**
     * Aggiunge un nodo alla mappa.
     */
    public void aggiungiNodo(NodoRete nodo) {
        if (nodo == null) {
            throw new IllegalArgumentException("Il nodo da aggiungere non può essere null.");
        }
        if (nodi.containsKey(nodo.getId())) {
            throw new IllegalArgumentException("Esiste già un nodo con ID: " + nodo.getId());
        }
        nodi.put(nodo.getId(), nodo);
        adiacenze.put(nodo.getId(), new ArrayList<>());
    }

    /**
     * Collega due nodi in modo bidirezionale.
     */
    public void collegaNodi(String idOrigine, String idDestinazione) {
        if (!nodi.containsKey(idOrigine) || !nodi.containsKey(idDestinazione)) {
            throw new IllegalArgumentException("Entrambi i nodi devono esistere nella mappa per essere collegati.");
        }
        if (!adiacenze.get(idOrigine).contains(idDestinazione)) {
            adiacenze.get(idOrigine).add(idDestinazione);
        }
        if (!adiacenze.get(idDestinazione).contains(idOrigine)) {
            adiacenze.get(idDestinazione).add(idOrigine);
        }
    }

    public NodoRete getNodo(String id) {
        return nodi.get(id);
    }

    public Collection<NodoRete> getTuttiINodi() {
        return Collections.unmodifiableCollection(nodi.values());
    }

    /**
     * Restituisce la lista di nodi adiacenti (collegati direttamente) a quello indicato.
     */
    public List<NodoRete> getNodiAdiacenti(String idNodo) {
        List<String> viciniIds = adiacenze.getOrDefault(idNodo, Collections.emptyList());
        List<NodoRete> vicini = new ArrayList<>();
        for (String id : viciniIds) {
            vicini.add(nodi.get(id));
        }
        return Collections.unmodifiableList(vicini);
    }
}