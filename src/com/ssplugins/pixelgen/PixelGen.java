package com.ssplugins.pixelgen;

import com.ssplugins.pixelgen.printers.ByColorPrinter;
import javafx.application.Application;
import javafx.stage.Stage;

public class PixelGen extends Application {
    
    public static void main(String[] args) {
        Application.launch(PixelGen.class, args);
    }
    
    @Override
    public void start(Stage stage) {
        int width = 256; // 1024
        int height = 128; // 864
        int colors = (int) Math.cbrt(width * height);
        
        Display display = new Display(stage, width, height, colors);
        Generator generator = new Generator(display);
        generator.startOnClick();
    
        ByColorPrinter printer = new ByColorPrinter();
        printer.addStartingPoint(new Pixel(width / 2, height / 2));
        
        generator.setPrinter(printer);
        
        display.build();
    }
    
}
