package it.unicam.cs.mpgc.rpg130572;

import it.unicam.cs.mpgc.rpg130572.controller.GestorePartita;
import it.unicam.cs.mpgc.rpg130572.model.*;
import it.unicam.cs.mpgc.rpg130572.persistence.CaricatoreMappaJson;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // 1. Caricamento dinamico della Mappa dal file JSON nelle risorse
        CaricatoreMappaJson caricatore = new CaricatoreMappaJson();
        MappaRete mappa = caricatore.caricaDaRisorse("mappa.json");

        // 2. Creazione dell'Hacker
        Hacker hacker = new Hacker( "CyberZero", 100, 2);

        // 3. Inizializzazione del Gestore Partita (Obiettivo: NODO_03)
        GestorePartita gestore = new GestorePartita(hacker, mappa, "NODO_03");
        gestore.spostaHacker("NODO_01");

        Scanner scanner = new Scanner(System.in);
        System.out.println("=== NETWORK HACKER (Mappa caricata da JSON) ===");
        System.out.println("Obiettivo: Violare il Mainframe Centrale (NODO_03)!\n");

        while (!gestore.isVittoria() && !gestore.isSconfitta()) {
            NodoRete nodoAttuale = hacker.getNodoCorrente();
            System.out.println("--------------------------------------------------");
            System.out.println("HACKER: " + hacker.getNickname() + " | Energia: " + hacker.getEnergia() + " | Abilità: " + hacker.getLivelloAbilita());
            System.out.println("POSIZIONE: " + nodoAttuale.ottieniDescrizione());

            System.out.println("\nCosa vuoi fare?");
            System.out.println("1) Tenta Hacking");
            System.out.println("2) Spostati in un nodo adiacente");
            System.out.print("Scelta: ");

            String scelta = scanner.nextLine();

            if (scelta.equals("1")) {
                System.out.println("\n[ESITO]: " + gestore.tentaHackingNodoCorrente());
            } else if (scelta.equals("2")) {
                List<NodoRete> vicini = mappa.getNodiAdiacenti(nodoAttuale.getId());
                System.out.println("\nNodi adiacenti disponibili:");
                for (NodoRete v : vicini) {
                    System.out.println("- ID: " + v.getId() + " | " + v.getNome() + " (Sicurezza: " + v.getLivelloSicurezza() + ")");
                }
                System.out.print("Inserisci l'ID della destinazione: ");
                String idDestinazione = scanner.nextLine();

                if (gestore.spostaHacker(idDestinazione)) {
                    System.out.println("\n[SPOSTAMENTO]: Spostato nel nodo " + idDestinazione);
                } else {
                    System.out.println("\n[ERRORE]: Nodo non raggiungibile!");
                }
            } else {
                System.out.println("\nScelta non valida!");
            }
        }

        System.out.println("\n==================================================");
        if (gestore.isVittoria()) {
            System.out.println(" VITTORIA! Hai violato il Mainframe Centrale!");
        } else {
            System.out.println("☠ SCONFITTA! Energia esaurita ☠");
        }
        System.out.println("==================================================");

        scanner.close();
    }
}
