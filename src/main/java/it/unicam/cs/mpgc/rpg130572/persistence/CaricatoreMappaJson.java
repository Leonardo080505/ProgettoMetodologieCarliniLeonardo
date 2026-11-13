package it.unicam.cs.mpgc.rpg130572.persistence;

import com.google.gson.*;
import it.unicam.cs.mpgc.rpg130572.model.*;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Gestisce il caricamento della MappaRete a partire da un file JSON situato nelle risorse.
 */
public class CaricatoreMappaJson {

    public MappaRete caricaDaRisorse(String nomeFile) {
        InputStream is = getClass().getClassLoader().getResourceAsStream(nomeFile);
        if (is == null) {
            throw new IllegalArgumentException("Impossibile trovare il file di risorsa: " + nomeFile);
        }

        try (Reader reader = new InputStreamReader(is)) {
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
            MappaRete mappa = new MappaRete();

            // 1. Lettura dei nodi
            JsonArray nodiArray = jsonObject.getAsJsonArray("nodi");
            for (JsonElement elem : nodiArray) {
                JsonObject obj = elem.getAsJsonObject();
                String tipo = obj.get("tipo").getAsString();
                String id = obj.get("id").getAsString();
                String nome = obj.get("nome").getAsString();
                int sicurezza = obj.get("livelloSicurezza").getAsInt();

                if ("FIREWALL".equalsIgnoreCase(tipo)) {
                    int danno = obj.get("danno").getAsInt();
                    mappa.aggiungiNodo(new Firewall(id, nome, sicurezza, danno));
                } else if ("SERVER".equalsIgnoreCase(tipo)) {
                    String dati = obj.get("contenutoDati").getAsString();
                    int ricompensa = obj.get("valoreRicompensa").getAsInt();
                    mappa.aggiungiNodo(new ServerDati(id, nome, sicurezza, dati, ricompensa));
                }
            }

            // 2. Lettura dei collegamenti
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