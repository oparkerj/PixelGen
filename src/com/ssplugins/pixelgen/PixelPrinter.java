package com.ssplugins.pixelgen;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class PixelPrinter {

    protected Display display;
    protected List<Color> colors;
    protected GraphicsContext context;
    protected int width;
    protected int height;
    
    private Color[][] grid;
    
    private int drawSize = 2000;
    private boolean active = false;
    private AnimationTimer drawLoop;
    private Queue<PixelData> output = new LinkedBlockingQueue<>();
    
    public abstract void print();
    
    public final void printTo(Display display) {
        this.display = display;
        Canvas canvas = display.getCanvas();
        width = (int) canvas.getWidth();
        height = (int) canvas.getHeight();
        context = canvas.getGraphicsContext2D();
        print();
    }
    
    public final List<Color> getColors() {
        return colors;
    }
    
    public final void setColors(List<Color> colors) {
        this.colors = colors;
    }
    
    //region Draw Loop Methods
    
    public final void setDrawSize(int drawSize) {
        this.drawSize = drawSize;
    }
    
    public final void beginDrawLoop() {
        if (drawLoop != null) {
            drawLoop.start();
            return;
        }
        active = true;
        drawLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                for (int i = 0; i < drawSize; i++) {
                    if (output.size() == 0) {
                        if (!active) {
                            this.stop();
                        }
                        return;
                    }
                    output.remove().directWriteTo(PixelPrinter.this);
                }
            }
        };
        drawLoop.start();
    }
    
    public final void endDrawLoop() {
        if (drawLoop == null) return;
        drawLoop.stop();
    }
    
    public final void finishDrawLoop() {
        active = false;
    }
    
    //endregion
    //region Grid Methods
    
    public final void initGrid() {
        grid = new Color[width][height];
    }
    
    public final boolean isValid(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }
    
    public final void writeGrid(Color color, int x, int y) {
        if (grid != null) {
            grid[x][y] = color;
        }
    }
    
    public final Color getColor(int x, int y) {
        return grid[x][y];
    }
    
    public final boolean isEmpty(Pixel pixel) {
        return getColor(pixel.getX(), pixel.getY()) == null;
    }
    
    public final Color[] surround(Pixel pixel) {
        return surround(pixel, Pixel.ALL);
    }
    
    public final Color[] surround(Pixel pixel, int sides) {
        return surround(pixel.getX(), pixel.getY(), sides);
    }
    
    public final Color[] surround(int x, int y, int sides) {
        Color[] around = new Color[9];
        surround(around, x, y, sides);
        return around;
    }
    
    public final Color[] surroundLarger(Pixel pixel, int size) {
        int sq = size * 2 + 1;
        sq *= sq;
        Color[] around = new Color[sq];
        surroundSize(around, pixel.getX(), pixel.getY(), size);
        return around;
    }
    
    
    public final void surround(Color[] arr, Pixel pixel, int sides) {
        surround(arr, pixel.getX(), pixel.getY(), sides);
    }
    
    public final void surround(Color[] arr, int x, int y, int sides) {
        int index = 0;
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++, index++) {
                arr[index] = null;
                if ((i == 0 && j == 0) || !isValid(x + i, y + j)) {
                    continue;
                }
                if (!Pixel.isSide(i, j, sides)) {
                    continue;
                }
                arr[index] = grid[x + i][y + j];
            }
        }
    }
    
    public final void surroundSize(Color[] arr, int x, int y, int size) {
        int index = 0;
        for (int i = -size; i < size + 1; i++) {
            for (int j = -size; j < size + 1; j++, index++) {
                arr[index] = null;
                if ((i == 0 && j == 0) || !isValid(x + i, y + j)) {
                    continue;
                }
                arr[index] = grid[x + i][y + j];
            }
        }
    }
    
    public final void surroundPixel(Pixel[] arr, Pixel center, int sides) {
        int index = 0;
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++, index++) {
                arr[index] = null;
                if ((i == 0 && j == 0) || !isValid(center.getX() + i, center.getY() + j)) {
                    continue;
                }
                if (!Pixel.isSide(i, j, sides)) {
                    continue;
                }
                arr[index] = new Pixel(center.getX() + i, center.getY() + j);
            }
        }
    }
    
    //endregion
    //region Write Methods
    
    public void queue(Color color, Pixel pixel) {
        queue(color, pixel.getX(), pixel.getY());
    }
    
    public void queue(Color color, int x, int y) {
        writeGrid(color, x, y);
        output.add(new PixelData(x, y, color));
    }
    
    public void directWrite(Color color, Pixel pixel) {
        directWrite(color, pixel.getX(), pixel.getY());
    }
    
    public void directWrite(Color color, int x, int y) {
        writeGrid(color, x, y);
        context.setFill(color);
        context.fillRect(x, y, 1, 1);
    }
    
    public void write(Color color, Pixel pixel) {
        write(color, pixel.getX(), pixel.getY());
    }
    
    public void write(Color color, int x, int y) {
        writeGrid(color, x, y);
        Platform.runLater(() -> {
            context.setFill(color);
            context.fillRect(x, y, 1, 1);
        });
    }
    
    //endregion
    //region Color Methods
    
    public double distance(Color a, Color b) {
        double red = a.getRed() - b.getRed();
        double green = a.getGreen() - b.getGreen();
        double blue = a.getBlue() - b.getBlue();
        return red * red + green * green + blue * blue;
    }
    
    public Color average(Color[] colors) {
        int total = 0;
        double r = 0;
        double g = 0;
        double b = 0;
        for (Color c : colors) {
            if (c == null) continue;
            total++;
            r += c.getRed();
            g += c.getGreen();
            b += c.getBlue();
        }
        return Color.color(r / total, g / total, b / total);
    }
    
    //endregion
    
}
