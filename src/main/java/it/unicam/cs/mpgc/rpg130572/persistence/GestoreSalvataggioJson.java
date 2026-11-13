package it.unicam.cs.mpgc.rpg130572.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import it.unicam.cs.mpgc.rpg130572.controller.GestorePartita;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Gestisce la scrittura dello stato corrente della partita su file JSON.
 */
public class GestoreSalvataggioJson {

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public void salvaPartita(GestorePartita gestore, String percorsoFile) {
        JsonObject jsonState = new JsonObject();

        // Salviamo lo stato dell'Hacker
        jsonState.addProperty("nicknameHacker", gestore.getHacker().getNickname());
        jsonState.addProperty("energiaHacker", gestore.getHacker().getEnergia());
        jsonState.addProperty("livelloAbilitaHacker", gestore.getHacker().getLivelloAbilita());

        if (gestore.getHacker().getNodoCorrente() != null) {
            jsonState.addProperty("nodoCorrenteId", gestore.getHacker().getNodoCorrente().getId());
        }

        jsonState.addProperty("idNodoObiettivo", gestore.getIdNodoObiettivo());

        // Scrittura su file
        try (FileWriter writer = new FileWriter(percorsoFile)) {
            gson.toJson(jsonState, writer);
        } catch (IOException e) {
            throw new RuntimeException("Errore durante il salvataggio della partita: " + e.getMessage(), e);
        }
    }
}