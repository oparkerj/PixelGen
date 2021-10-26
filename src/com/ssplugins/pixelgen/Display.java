package com.ssplugins.pixelgen;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

public class Display {
    
    private Stage stage;
    private int width;
    private int height;
    
    private Scene scene;
    private Pane pane;
    
    private boolean started = false;
    private Consumer<MouseEvent> startCallback;
    
    private Canvas canvas;
    
    public Display(Stage stage, int width, int height) {
        this.stage = stage;
        this.width = width;
        this.height = height;
    }
    
    public void build() {
        pane = new Pane();
        scene = new Scene(pane, width, height);
        canvas = new Canvas(width, height);
        pane.getChildren().add(canvas);
    
        ContextMenu menu = new ContextMenu();
        MenuItem saveImage = new MenuItem("Save Image");
        saveImage.setOnAction(event -> saveCanvasImage());
        menu.getItems().add(saveImage);
        canvas.setOnContextMenuRequested(event -> menu.show(canvas, event.getScreenX(), event.getScreenY()));
    
        canvas.setOnMouseClicked(event -> {
            menu.hide();
            if (started) return;
            started = true;
            if (startCallback != null) startCallback.accept(event);
        });
    
        stage.setScene(scene);
        stage.show();
    }
    
    public void saveCanvasImage() {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG Files", "*.png"));
        File file = chooser.showSaveDialog(stage);
        if (file == null) return;
        WritableImage writableImage = new WritableImage(width, height);
        canvas.snapshot(null, writableImage);
        BufferedImage image = SwingFXUtils.fromFXImage(writableImage, null);
        try {
            ImageIO.write(image, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void setTitle(String title) {
        Platform.runLater(() -> stage.setTitle(title));
    }
    
    public int getColors() {
        return (int) Math.cbrt(width * height);
    }
    
    public Canvas getCanvas() {
        return canvas;
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
    
    public void setStartCallback(Runnable runnable) {
        this.startCallback = mouseEvent -> runnable.run();
    }
    
    public void setStartCallback(Consumer<MouseEvent> startCallback) {
        this.startCallback = startCallback;
    }
    
}
