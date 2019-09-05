package com.ssplugins.pixelgen.printers;

import com.ssplugins.pixelgen.Pixel;
import com.ssplugins.pixelgen.PixelPrinter;
import javafx.scene.paint.Color;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class By3DColorPrinter extends PixelPrinter {

    private Color[][][] palette;
    private Deque<Pixel> startingPoints = new LinkedList<>();
    private Set<Pixel> next = new LinkedHashSet<>();
    
    private Pixel[] pixels = new Pixel[9];
    
    @Override
    public void print() {
        initGrid();
        
        int depth = display.getColors();
        palette = new Color[depth][depth][depth];
        int x = 0;
        int y = 0;
        int z = 0;
        for (Color color : colors) {
            palette[x][y][z] = color;
            x++;
            if (x == depth) {
                x = 0;
                y++;
                if (y == depth) {
                    y = 0;
                    z++;
                }
            }
        }
    
        while (!startingPoints.isEmpty()) {
            Pixel pixel = startingPoints.remove();
            Color c = colors.get(ThreadLocalRandom.current().nextInt(colors.size()));
            queue(c, pixel);
            Pixel3D where = new Pixel3D(c, depth);
            palette[where.getX()][where.getY()][where.getZ()] = null;
            surroundPixel(pixels, pixel, Pixel.ALL);
            for (Pixel p : pixels) {
                if (p == null) continue;
                next.add(p);
            }
        }
        
        beginDrawLoop();
        while (!next.isEmpty()) {
            Iterator<Pixel> it = next.iterator();
            Pixel next = it.next();
            it.remove();
            Color start = average(surround(next, Pixel.ALL));
            queue(getNearest3D(start), next);
            surroundPixel(pixels, next, Pixel.ALL);
            for (Pixel p : pixels) {
                if (p == null || !isEmpty(p)) continue;
                this.next.add(p);
            }
        }
        System.out.println("Finished");
        finishDrawLoop();
    }
    
    private Color getNearest3D(Color start) {
        Set<Pixel3D> next = new HashSet<>();
        Set<Pixel3D> buffer = new HashSet<>();
        Set<Pixel3D> searched = new HashSet<>();
        int depth = display.getColors() - 1;
        next.add(new Pixel3D(start.getRed() * depth, start.getGreen() * depth, start.getBlue() * depth));
        while (!next.isEmpty()) {
            Optional<Pixel3D> first = next.parallelStream().filter(p -> palette[p.getX()][p.getY()][p.getZ()] != null).findFirst();
            if (first.isPresent()) {
                Pixel3D p = first.get();
                Color c = palette[p.getX()][p.getY()][p.getZ()];
                palette[p.getX()][p.getY()][p.getZ()] = null;
                return c;
            }
            Set<Pixel3D> finalBuffer = buffer;
            next.forEach(p -> addSurround(p, finalBuffer, searched));
            Set<Pixel3D> set = next;
            next = buffer;
            buffer = set;
            buffer.clear();
        }
        throw new IllegalArgumentException("Unable to find nearest color.");
    }
    
    private void addSurround(Pixel3D p, Set<Pixel3D> next, Set<Pixel3D> searched) {
        Pixel3D[] dirs = {p.up(), p.down(), p.left(), p.right(), p.top(), p.bottom()};
        for (Pixel3D d : dirs) {
            if (searched.contains(d) || !inRange(d)) continue;
            next.add(d);
        }
    }
    
    private boolean inRange(Pixel3D p) {
        int depth = display.getColors();
        return p.getX() >= 0 && p.getX() < depth &&
                p.getY() >= 0 && p.getY() < depth &&
                p.getZ() >= 0 && p.getZ() < depth;
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
    
        public Pixel3D(double x, double y, double z) {
            this((int) x, (int) y, (int) z);
        }
    
        public Pixel3D(Color c, int depth) {
            this(c.getRed() * (depth - 1), c.getGreen() * (depth - 1), c.getBlue() * (depth - 1));
        }
    
        public Pixel3D up() {
            return new Pixel3D(getX(), getY() + 1, z);
        }
        
        public Pixel3D down() {
            return new Pixel3D(getX(), getY() - 1, z);
        }
        
        public Pixel3D right() {
            return new Pixel3D(getX() + 1, getY(), z);
        }
        
        public Pixel3D left() {
            return new Pixel3D(getX() - 1, getY(), z);
        }
        
        public Pixel3D top() {
            return new Pixel3D(getX(), getY(), z + 1);
        }
        
        public Pixel3D bottom() {
            return new Pixel3D(getX(), getY(), z - 1);
        }
    
        @Override
        public int hashCode() {
            return Objects.hash(getX(), getY(), z);
        }
    
        @Override
        public boolean equals(Object obj) {
            return obj instanceof Pixel3D && ((Pixel3D) obj).z == z &&  super.equals(obj);
        }
    
        public int getZ() {
            return z;
        }
    
    }
    
}
