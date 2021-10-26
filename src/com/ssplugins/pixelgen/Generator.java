package com.ssplugins.pixelgen;

import com.ssplugins.pixelgen.colorgen.PaletteGenerator;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Generator extends Thread {
    
    private Display display;
    private PaletteGenerator paletteGenerator;
    
    private PixelPrinter printer;
    
    public Generator(Display display, PaletteGenerator palette) {
        this.display = display;
        this.paletteGenerator = palette;
        this.setDaemon(true);
    }
    
    @Override
    public void run() {
        List<Color> colors = paletteGenerator.getPalette(display, display.getWidth(), display.getHeight());
        printer.setColors(colors);
        printer.printTo(display);
    }
    
    public void startOnClick() {
        display.setStartCallback(this::start);
    }
    
    public void onFirstClick(Consumer<MouseEvent> c) {
        display.setStartCallback(c);
    }
    
    public PixelPrinter getPrinter() {
        return printer;
    }
    
    public void setPrinter(PixelPrinter printer) {
        this.printer = printer;
    }
    
}
