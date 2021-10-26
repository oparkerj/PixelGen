package com.ssplugins.pixelgen.printers;

import com.ssplugins.pixelgen.Pixel;
import com.ssplugins.pixelgen.PixelPrinter;
import javafx.scene.paint.Color;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

public class MultiByColorPrinter extends PixelPrinter {
    
    private List<Pixel> startingPoints = new LinkedList<>();
    private int side = Pixel.ALL;
    private int compareSide = Pixel.ALL;
    
    private Queue<Pixel> next = new LinkedBlockingQueue<>();
    
    private Pixel[] pixels = new Pixel[9];
    
    @Override
    public void print() {
        // Set up queue
        if (startingPoints.size() == 0) {
            throw new IllegalArgumentException("No starting points.");
        }
        
        // Place pixels
        initGrid();
        beginDrawLoop();
        Collections.shuffle(colors);
//        colors.sort(Comparing.value(Color::getSaturation));
        
        startingPoints.forEach(pixel -> {
        
        });
        
        colors.parallelStream().forEach(color -> {
        
        });
        
        
        while (colors.size() > 0) {
            Color color = colors.remove(0);
            Pixel where;
            if (next.size() == 0) {
                where = startingPoints.remove(0);
            }
            else {
                Optional<Pixel> min = next.parallelStream().min(Comparator.comparingDouble(value -> distance(color, value.fastAverageIn(this))));
//                Optional<Pixel> min = next.stream().parallel().min(Comparator.comparingDouble(value -> value.minimumCompare(this, compareSide, color)));
//                Optional<Pixel> min = next.parallelStream().min(Comparator.comparingDouble(value -> value.averageCompare(this, compareSide, color)));
                if (!min.isPresent()) throw new IllegalArgumentException("Could not find placement for pixel.");
                where = min.get();
            }
            queue(color, where);
            next.remove(where);
            surroundPixel(pixels, where, side);
            for (Pixel p : pixels) {
                if (p == null || !isEmpty(p)) continue;
                next.add(p);
            }
        }
        finishDrawLoop();
    }
    
    public void addStartingPoint(Pixel pixel) {
        startingPoints.add(pixel);
    }
    
    public int getSide() {
        return side;
    }
    
    public void setSide(int side) {
        this.side = side;
    }
    
    public int getCompareSide() {
        return compareSide;
    }
    
    public void setCompareSide(int compareSide) {
        this.compareSide = compareSide;
    }
    
}
