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

            JsonObject jsonHacker = json.getAsJsonObject("hacker");
            String nickname = jsonHacker.get("nickname").getAsString();
            int energia = jsonHacker.get("energia").getAsInt();
            int abilita = jsonHacker.get("abilita").getAsInt();
            String nodoCorrenteId = jsonHacker.get("nodoCorrenteId").getAsString();

            String idNodoObiettivo = json.get("idNodoObiettivo").getAsString();

            Hacker hacker = new Hacker(nickname, energia, abilita);
            GestorePartita gestore = new GestorePartita(hacker, mappa, idNodoObiettivo);
            gestore.spostaHacker(nodoCorrenteId);

            return gestore;
        } catch (IOException e) {
            throw new RuntimeException("Impossibile leggere il file di salvataggio: " + e.getMessage(), e);
        }
    }
}