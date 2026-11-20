package it.unicam.cs.mpgc.rpg130572.model;

import java.util.*;

/**
 * Rappresenta la struttura a grafo della rete di nodi e gestisce le connessioni tra di essi.
 */
public class MappaRete {

    private final Map<String, NodoRete> nodi;
    private final Map<String, List<String>> adiacenze;

    public MappaRete() {
        this.nodi = new HashMap<>();
        this.adiacenze = new HashMap<>();
    }

    public void aggiungiNodo(NodoRete nodo) {
        if (nodo == null) {
            throw new IllegalArgumentException("Impossibile aggiungre un nodo null.");
        }
        nodi.put(nodo.getId(), nodo);
        adiacenze.putIfAbsent(nodo.getId(), new ArrayList<>());
    }

    public void collegaNodi(String idOrigine, String idDestinazione) {
        if (!nodi.containsKey(idOrigine) || !nodi.containsKey(idDestinazione)) {
            throw new IllegalArgumentException("Entrambi i nodi devono esistere per creare un collegamento.");
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

    public List<NodoRete> getNodiAdiacenti(String idNodo) {
        List<String> viciniIds = adiacenze.getOrDefault(idNodo, Collections.emptyList());
        List<NodoRete> vicini = new ArrayList<>();
        for (String id : viciniIds) {
            vicini.add(nodi.get(id));
        }
        return Collections.unmodifiableList(vicini);
    }
}