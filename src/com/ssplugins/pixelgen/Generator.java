package com.ssplugins.pixelgen;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Generator extends Thread {
    
    private Display display;
    
    private PixelPrinter printer;
    
    public Generator(Display display) {
        this.display = display;
        this.setDaemon(true);
    }
    
    @Override
    public void run() {
        List<Color> colors = new ArrayList<>(display.getWidth() * display.getHeight());
        int amount = display.getColors();
        double delta = 1.0 / (amount - 1);
        for (int r = 0; r < amount; r++) {
            for (int g = 0; g < amount; g++) {
                for (int b = 0; b < amount; b++) {
                    colors.add(Color.color(r * delta, g * delta, b * delta));
                }
            }
        }
        printer.setColors(colors);
        printer.printTo(display);
    }
    
    public void startOnClick() {
        display.setStartCallback(this::start);
    }
    
    public PixelPrinter getPrinter() {
        return printer;
    }
    
    public void setPrinter(PixelPrinter printer) {
        this.printer = printer;
    }
    
}
