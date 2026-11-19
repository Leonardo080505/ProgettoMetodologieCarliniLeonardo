package it.unicam.cs.mpgc.rpg130572.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HackerTest {

    private Hacker hacker;

    @BeforeEach
    void setUp() {
        hacker = new Hacker("CyberZero", 100, 2);
    }

    @Test
    void testInizializzazioneHacker() {
        assertEquals("CyberZero", hacker.getNickname());
        assertEquals(100, hacker.getEnergia());
        assertEquals(2, hacker.getLivelloAbilita());
    }

    @Test
    void testRiduciEnergia() {
        hacker.riduciEnergia(30);
        assertEquals(70, hacker.getEnergia());

        hacker.riduciEnergia(100);
        assertEquals(0, hacker.getEnergia());
    }

    @Test
    void testIncrementaAbilita() {
        assertEquals(2, hacker.getLivelloAbilita());
        hacker.incrementaAbilita();
        assertEquals(3, hacker.getLivelloAbilita());
    }
}