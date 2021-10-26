package com.ssplugins.pixelgen.colorgen;

import com.ssplugins.pixelgen.Display;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Shades implements PaletteGenerator {
    
    private Color baseColor;
    
    public Shades(Color baseColor) {
        this.baseColor = baseColor;
    }
    
    @Override
    public List<Color> getPalette(Display display, int width, int height) {
        int shades = width * height;
        List<Color> result = new ArrayList<>(shades);
        
        double hue = baseColor.getHue();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                result.add(Color.hsb(hue, x / (double) width, y / (double) height));
            }
        }
        return result;
    }
    
}
