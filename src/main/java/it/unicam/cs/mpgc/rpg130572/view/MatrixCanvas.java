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

        AnimationTimer timer = new AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {
                // Aggiorna l'animazione a ~20 FPS per un effetto fluido e non troppo frenetico
                if (now - lastUpdate >= 50_000_000) {
                    disegna();
                    lastUpdate = now;
                }
            }
        };
        timer.start();
    }

    private void inizializza(double width, double height) {
        setWidth(width);
        setHeight(height);
        this.columns = (int) (width / fontSize) + 1;
        this.drops = new int[columns];
        for (int i = 0; i < columns; i++) {
            drops[i] = random.nextInt((int) (height / fontSize) + 1);
        }
    }

    private void disegna() {
        // Applica un velo nero semi-trasparente ad ogni frame per l'effetto scia
        gc.setFill(Color.rgb(11, 12, 16, 0.15));
        gc.fillRect(0, 0, getWidth(), getHeight());

        gc.setFont(Font.font("Consolas", fontSize));

        for (int i = 0; i < columns; i++) {
            // Genera '0' o '1' casuali
            String charBinario = random.nextBoolean() ? "1" : "0";
            double x = i * fontSize;
            double y = drops[i] * fontSize;

            // Il primo carattere in basso della scia è più brillante
            gc.setFill(Color.rgb(102, 252, 241, 0.9));
            gc.fillText(charBinario, x, y);

            if (y > getHeight() && random.nextDouble() > 0.975) {
                drops[i] = 0;
            }
            drops[i]++;
        }
    }
}