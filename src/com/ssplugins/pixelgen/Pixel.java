package com.ssplugins.pixelgen;

import javafx.scene.paint.Color;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.DoubleStream;

public class Pixel {
    
    public static final int ALL = 1;
    public static final int EDGE = 0;
    public static final int CORNER = -1;
    
    private int x;
    private int y;
    
    public Pixel(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public static boolean isSide(int dx, int dy, int side) {
        if (side > 0) return true;
        if (side < 0) return dx != 0 && dy != 0;
        return (dx == 0) ^ (dy == 0);
    }
    
    public Color getColor(PixelPrinter printer) {
        return printer.getColor(x, y);
    }
    
    public Color averageIn(Color[] buffer, PixelPrinter printer, int sides) {
        printer.surround(buffer, this, sides);
        return printer.average(buffer);
    }
    
    public Color averageIn(PixelPrinter printer, int sides) {
        Color[] buffer = printer.surround(this, sides);
        return printer.average(buffer);
    }
    
    public Color fastAverageIn(PixelPrinter printer) {
        return printer.fastAverage(x, y);
    }
    
    public double minimumCompare(PixelPrinter printer, int sides, Color compare) {
        Color[] buffer = printer.surround(this, sides);
        return Arrays.stream(buffer).filter(Objects::nonNull).mapToDouble(color -> printer.distance(color, compare)).min().orElse(0);
    }
    
    public double averageCompare(PixelPrinter printer, int sides, Color compare) {
        Color[] buffer = printer.surround(this, sides);
        return Arrays.stream(buffer).filter(Objects::nonNull).mapToDouble(color -> printer.distance(color, compare)).average().orElse(0);
    }
    
    public Color largeAverageIn(PixelPrinter printer, int size) {
        Color[] buffer = printer.surroundLarger(this, size);
        return printer.average(buffer);
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
    
    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Pixel) && ((Pixel) obj).x == x && ((Pixel) obj).y == y;
    }
    
}
