package com.ssplugins.pixelgen;

import com.ssplugins.pixelgen.colorgen.EveryColor;
import com.ssplugins.pixelgen.colorgen.Shades;
import com.ssplugins.pixelgen.printers.ByColorPrinter;
import com.ssplugins.pixelgen.printers.MultiByColorPrinter;
import com.ssplugins.pixelgen.printers.SimplePixelPrinter;
import com.ssplugins.pixelgen.util.Comparing;
import javafx.application.Application;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class PixelGen extends Application {
    
    public static void main(String[] args) {
        Application.launch(PixelGen.class, args);
    }
    
    @Override
    public void start(Stage stage) {
//        int width = 256; // 1024
//        int height = 128; // 864
        
        int width = 500;
        int height = 500;
        
        Display display = new Display(stage, width, height);
        Generator generator = new Generator(display, new Shades(Color.BLUE));
        generator.startOnClick();
    
//        MultiByColorPrinter printer = new MultiByColorPrinter();
        ByColorPrinter printer = new ByColorPrinter();
//        SimplePixelPrinter printer = new SimplePixelPrinter();
//        printer.setSorter(Comparing.value(Color::getSaturation));
        printer.addStartingPoint(new Pixel(width / 2, height / 2));
//        printer.addStartingPoint(new Pixel(0, 0));
        
        generator.setPrinter(printer);
        
        display.build();
    }
    
}
