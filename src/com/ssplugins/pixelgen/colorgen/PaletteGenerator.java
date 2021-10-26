package com.ssplugins.pixelgen.colorgen;

import com.ssplugins.pixelgen.Display;
import javafx.scene.paint.Color;

import java.util.List;

public interface PaletteGenerator {
    
    List<Color> getPalette(Display display, int width, int height);
    
}
