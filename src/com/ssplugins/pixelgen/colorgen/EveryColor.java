package com.ssplugins.pixelgen.colorgen;

import com.ssplugins.pixelgen.Display;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class EveryColor implements PaletteGenerator {
    
    @Override
    public List<Color> getPalette(Display display, int width, int height) {
        List<Color> result = new ArrayList<>(width * height);
        int amount = (int) Math.cbrt(width * height);
        double delta = 1.0 / (amount - 1);
        for (int r = 0; r < amount; r++) {
            for (int g = 0; g < amount; g++) {
                for (int b = 0; b < amount; b++) {
                    result.add(Color.color(r * delta, g * delta, b * delta));
                }
            }
        }
        return result;
    }
    
}
