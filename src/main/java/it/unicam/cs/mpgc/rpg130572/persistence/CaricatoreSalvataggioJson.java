package it.unicam.cs.mpgc.rpg130572.persistence;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.unicam.cs.mpgc.rpg130572.controller.GestorePartita;
import it.unicam.cs.mpgc.rpg130572.model.Hacker;
import it.unicam.cs.mpgc.rpg130572.model.MappaRete;

import java.io.FileReader;
import java.io.IOException;

/**
 * Carica lo stato di una partita salvata in precedenza da file JSON.
 */
public class CaricatoreSalvataggioJson {

    private final Gson gson = new Gson();

    public GestorePartita caricaPartita(String percorsoSalvataggio, MappaRete mappa) {
        try (FileReader reader = new FileReader(percorsoSalvataggio)) {
            JsonObject json = gson.fromJson(reader, JsonObject.class);

            String nickname = json.get("nicknameHacker").getAsString();
            int energia = json.get("energiaHacker").getAsInt();
            int abilita = json.get("livelloAbilitaHacker").getAsInt();
            String nodoCorrenteId = json.has("nodoCorrenteId") ? json.get("nodoCorrenteId").getAsString() : "NODO_01";
            String idNodoObiettivo = json.has("idNodoObiettivo") ? json.get("idNodoObiettivo").getAsString() : "NODO_03";

            Hacker hacker = new Hacker(nickname, energia, abilita);
            GestorePartita gestore = new GestorePartita(hacker, mappa, idNodoObiettivo);
            gestore.spostaHacker(nodoCorrenteId);

            return gestore;
        } catch (IOException e) {
            throw new RuntimeException("Impossibile leggere il file di salvataggio: " + e.getMessage(), e);
        }
    }
}