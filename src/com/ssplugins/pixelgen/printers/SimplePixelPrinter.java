package com.ssplugins.pixelgen.printers;

import com.ssplugins.pixelgen.PixelPrinter;
import javafx.scene.paint.Color;

import java.util.Comparator;

public class SimplePixelPrinter extends PixelPrinter {
    
    private Comparator<Color> sorter;
    
    @Override
    public void print() {
        if (sorter != null) {
            colors.sort(sorter);
        }
        beginDrawLoop();
        int index = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                queue(colors.get(index++), x, y);
            }
        }
        finishDrawLoop();
    }
    
    public void setSorter(Comparator<Color> sorter) {
        this.sorter = sorter;
    }
    
}
