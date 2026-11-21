package it.unicam.cs.mpgc.rpg130572;

import it.unicam.cs.mpgc.rpg130572.controller.GestorePartita;
import it.unicam.cs.mpgc.rpg130572.model.*;
import it.unicam.cs.mpgc.rpg130572.persistence.CaricatoreMappa;
import it.unicam.cs.mpgc.rpg130572.persistence.CaricatoreMappaJson;

import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        CaricatoreMappa caricatore = new CaricatoreMappaJson();
        MappaRete mappa = caricatore.caricaDaRisorse("mappa.json");

        Hacker hacker = new Hacker("CyberZero", 100, 2);
        GestorePartita gestore = new GestorePartita(hacker, mappa, "NODO_13");
        gestore.spostaHacker("NODO_01");

        Scanner scanner = new Scanner(System.in);

        System.out.println("=== NETWORK HACKER  ===");
        System.out.println("Obiettivo: Violare il Mainframe Riservato (NODO_13)!\n");

        while (!gestore.isVittoria() && !gestore.isSconfitta()) {
            NodoRete nodoAttuale = hacker.getNodoCorrente();
            System.out.println("--------------------------------------------------");
            System.out.println("HACKER: " + hacker.getNickname() + " | Energia: " + hacker.getEnergia() + " | Abilità: " + hacker.getLivelloAbilita());
            System.out.println("POSIZIONE ATTUALE: [" + nodoAttuale.getId() + "] " + nodoAttuale.getNome() + " (Sicurezza: " + nodoAttuale.getLivelloSicurezza() + ")");
            System.out.println("STATO NODO: " + (nodoAttuale.isHackerato() ? "VIOLATO " : "NON VIOLATO "));
            System.out.println("--------------------------------------------------");
            System.out.println("Azioni disponibili:");
            System.out.println("1. Tenta Hacking nodo corrente");
            System.out.println("2. Scansiona nodo target");
            System.out.println("3. Spostati in un altro nodo");
            System.out.print("Scegli un'opzione (1-3): ");

            String scelta = scanner.nextLine();

            if ("1".equals(scelta)) {
                String esito = gestore.tentaHackingNodoCorrente();
                System.out.println("\n[HACKING]: " + esito);
            } else if ("2".equals(scelta)) {
                System.out.print("Inserisci l'ID del nodo da scansionare: ");
                String idTarget = scanner.nextLine();
                String esitoScansione = gestore.scansionaNodoTarget(idTarget);
                System.out.println("\n[SCANSIONE]: " + esitoScansione);
            } else if ("3".equals(scelta)) {
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
            System.out.println(" VITTORIA! Hai violato il Mainframe Riservato (NODO_13)!");
        } else {
            System.out.println(" SCONFITTA! Energia esaurita ");
        }
        System.out.println("==================================================");

        scanner.close();
    }
}