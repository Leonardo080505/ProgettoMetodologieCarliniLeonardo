package it.unicam.cs.mpgc.rpg130572.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import it.unicam.cs.mpgc.rpg130572.controller.GestorePartita;
import it.unicam.cs.mpgc.rpg130572.model.Hacker;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Salva lo stato corrente della partita in un file.
 */
public class GestoreSalvataggioJson {

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public void salvaPartita(GestorePartita gestore, String percorsoSalvataggio) {
        Hacker hacker = gestore.getHacker();
        JsonObject root = new JsonObject();

        JsonObject jsonHacker = new JsonObject();
        jsonHacker.addProperty("nickname", hacker.getNickname());
        jsonHacker.addProperty("energia", hacker.getEnergia());
        jsonHacker.addProperty("abilita", hacker.getLivelloAbilita());
        jsonHacker.addProperty("nodoCorrenteId", hacker.getNodoCorrente() != null ? hacker.getNodoCorrente().getId() : "");

        root.add("hacker", jsonHacker);
        root.addProperty("idNodoObiettivo", gestore.getIdNodoObiettivo());

        try (FileWriter writer = new FileWriter(percorsoSalvataggio)) {
            gson.toJson(root, writer);
        } catch (IOException e) {
            throw new RuntimeException("Errore durante il salvataggio della partita: " + e.getMessage(), e);
        }
    }
}