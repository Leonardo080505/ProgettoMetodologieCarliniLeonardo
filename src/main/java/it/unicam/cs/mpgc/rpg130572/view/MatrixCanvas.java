package it.unicam.cs.mpgc.rpg130572.view;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.Random;

/**
 * Canvas animato che genera l'effetto pioggia di codice binario stile Matrix.
 */
public class MatrixCanvas extends Canvas {

    private final int fontSize = 14;
    private int columns;
    private int[] drops;
    private final Random random = new Random();
    private final GraphicsContext gc;

    public MatrixCanvas(double width, double height) {
        super(width, height);
        this.gc = getGraphicsContext2D();

        inizializza(width, height);

        widthProperty().addListener(obs -> ridimensiona(getWidth(), getHeight()));
        heightProperty().addListener(obs -> ridimensiona(getWidth(), getHeight()));

        AnimationTimer timer = new AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {
                if (now - lastUpdate >= 50_000_000) {
                    disegna();
                    lastUpdate = now;
                }
            }
        };
        timer.start();
    }

    @Override
    public boolean isResizable() {
        return true;
    }

    @Override
    public double prefWidth(double height) {
        return getWidth();
    }

    @Override
    public double prefHeight(double width) {
        return getHeight();
    }

    private void inizializza(double width, double height) {
        if (width <= 0 || height <= 0) return;
        this.columns = (int) (width / fontSize) + 1;
        this.drops = new int[columns];
        for (int i = 0; i < columns; i++) {
            drops[i] = random.nextInt((int) (height / fontSize) + 1);
        }
    }

    private synchronized void ridimensiona(double nuovaLarghezza, double nuovaAltezza) {
        if (nuovaLarghezza <= 0 || nuovaAltezza <= 0) return;

        int nuoveColonne = (int) (nuovaLarghezza / fontSize) + 1;
        int[] nuoviDrops = new int[nuoveColonne];

        for (int i = 0; i < nuoveColonne; i++) {
            if (drops != null && i < drops.length) {
                nuoviDrops[i] = drops[i];
            } else {
                nuoviDrops[i] = random.nextInt((int) (nuovaAltezza / fontSize) + 1);
            }
        }

        this.columns = nuoveColonne;
        this.drops = nuoviDrops;
    }

    private synchronized void disegna() {
        if (drops == null || columns == 0) return;

        gc.setFill(Color.rgb(11, 12, 16, 0.15));
        gc.fillRect(0, 0, getWidth(), getHeight());

        gc.setFont(Font.font("Consolas", fontSize));

        for (int i = 0; i < columns && i < drops.length; i++) {
            String charBinario = random.nextBoolean() ? "1" : "0";
            double x = i * fontSize;
            double y = drops[i] * fontSize;

            gc.setFill(Color.rgb(102, 252, 241, 0.9));
            gc.fillText(charBinario, x, y);

            if (y > getHeight() && random.nextDouble() > 0.975) {
                drops[i] = 0;
            }
            drops[i]++;
        }
    }
}