package com.ssplugins.pixelgen.printers;

import com.ssplugins.pixelgen.Pixel;
import com.ssplugins.pixelgen.PixelPrinter;
import javafx.scene.paint.Color;

import java.util.*;

public class By3DColorPrinter extends PixelPrinter {

    private Color[][][] palette;
    private Deque<Pixel> startingPoints = new LinkedList<>();
    private Set<Pixel> next = new LinkedHashSet<>();
    
    private Pixel[] pixels = new Pixel[9];
    
    @Override
    public void print() {
        int depth = display.getColors();
        palette = new Color[depth][depth][depth];
        int x = 0;
        int y = 0;
        int z = 0;
        for (Color color : colors) {
            palette[x][y][z] = color;
            x++;
            if (x == depth) {
                y++;
                if (y == depth) z++;
            }
        }
    
        while (!startingPoints.isEmpty()) {
            Pixel pixel = startingPoints.remove();
            surroundPixel(pixels, pixel, Pixel.ALL);
            for (Pixel p : pixels) {
                if (p == null) continue;
                next.add(p);
            }
        }
    
        while (!next.isEmpty()) {
            Iterator<Pixel> it = next.iterator();
            Pixel next = it.next();
            it.remove();
            //
        }
    }
    
    public void addStartingPoint(Pixel pixel) {
        startingPoints.add(pixel);
    }
    
    private class Pixel3D extends Pixel {
        
        private int z;
    
        public Pixel3D(int x, int y, int z) {
            super(x, y);
            this.z = z;
        }
    
        public int getZ() {
            return z;
        }
    
    }
    
}
