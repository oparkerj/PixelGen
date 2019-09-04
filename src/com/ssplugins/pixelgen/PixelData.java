package com.ssplugins.pixelgen;

import javafx.scene.paint.Color;

import java.util.Objects;

public class PixelData extends Pixel {
    
    private Color color;
    
    public PixelData(int x, int y, Color color) {
        super(x, y);
        this.color = color;
    }
    
    public PixelData(Pixel pixel, Color color) {
        this(pixel.getX(), pixel.getY(), color);
    }
    
    public void directWriteTo(PixelPrinter printer) {
        printer.directWrite(color, getX(), getY());
    }
    
    public void writeTo(PixelPrinter printer) {
        printer.write(color, getX(), getY());
    }
    
    public Color getColor() {
        return color;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(color, getX(), getY());
    }
    
    @Override
    public boolean equals(Object obj) {
        return (obj instanceof PixelData) && ((PixelData) obj).color == color && super.equals(obj);
    }
    
}
