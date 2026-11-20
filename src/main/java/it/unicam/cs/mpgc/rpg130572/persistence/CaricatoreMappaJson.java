package it.unicam.cs.mpgc.rpg130572.persistence;

import com.google.gson.*;
import it.unicam.cs.mpgc.rpg130572.model.*;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Gestisce il caricamento della mappa di rete da file , implementando l'interfaccia CaricatoreMappa.
 */
public class CaricatoreMappaJson implements CaricatoreMappa {

    private static class NodoBase extends NodoRete {
        public NodoBase(String id, String nome, int livelloSicurezza, String tipo) {
            super(id, nome, livelloSicurezza, tipo);
        }

        @Override
        public String ottieniDescrizione() {
            return getId() + " - " + getNome() + " [Sicurezza: " + getLivelloSicurezza() + "]";
        }
    }

    @Override
    public MappaRete caricaDaRisorse(String nomeFile) {
        InputStream is = getClass().getClassLoader().getResourceAsStream(nomeFile);
        if (is == null) {
            throw new IllegalArgumentException("Impossibile trovare il file di risorsa: " + nomeFile);
        }

        try (Reader reader = new InputStreamReader(is)) {
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
            MappaRete mappa = new MappaRete();

            JsonArray nodiArray = jsonObject.getAsJsonArray("nodi");
            for (JsonElement elem : nodiArray) {
                JsonObject obj = elem.getAsJsonObject();
                String tipo = obj.has("tipo") ? obj.get("tipo").getAsString() : "NORMALE";
                String id = obj.get("id").getAsString();
                String nome = obj.get("nome").getAsString();
                int sicurezza = obj.get("livelloSicurezza").getAsInt();

                NodoRete nodoGenerato;

                if ("FIREWALL".equalsIgnoreCase(tipo)) {
                    int danno = obj.has("danno") ? obj.get("danno").getAsInt() : 15;
                    nodoGenerato = new Firewall(id, nome, sicurezza, danno);
                } else if ("SERVER".equalsIgnoreCase(tipo)) {
                    String dati = obj.has("contenutoDati") ? obj.get("contenutoDati").getAsString() : "Dati segreti";
                    int ricompensa = obj.has("valoreRicompensa") ? obj.get("valoreRicompensa").getAsInt() : 10;
                    nodoGenerato = new ServerDati(id, nome, sicurezza, dati, ricompensa);
                } else {
                    nodoGenerato = new NodoBase(id, nome, sicurezza, tipo);
                }

                mappa.aggiungiNodo(nodoGenerato);
            }

            JsonArray collegamentiArray = jsonObject.getAsJsonArray("collegamenti");
            for (JsonElement elem : collegamentiArray) {
                JsonObject obj = elem.getAsJsonObject();
                String da = obj.get("da").getAsString();
                String a = obj.get("a").getAsString();
                mappa.collegaNodi(da, a);
            }

            return mappa;
        } catch (Exception e) {
            throw new RuntimeException("Errore durante la lettura del file JSON: " + e.getMessage(), e);
        }
    }
}